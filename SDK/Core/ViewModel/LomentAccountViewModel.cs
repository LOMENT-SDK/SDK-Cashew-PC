using Cashew;
using Cashew.Model;
using Cashew.Services;
using Cashew.Utility.Helper;
using Cashew.Utility.Services;
using Core.Data;
using Core.Services;
using GalaSoft.MvvmLight;
using GalaSoft.MvvmLight.Command;
using GalaSoft.MvvmLight.Ioc;
using Loment.CashewReference.DB.Model;
using Loment.CashewReference.DB.Services;
using Microsoft.Practices.ServiceLocation;
using System;
using System.Collections.ObjectModel;
using System.Linq;
using System.Threading.Tasks;

namespace Core.ViewModel
{
    public class LomentAccountViewModel : ViewModelBase
    {
        private readonly INavigationService _navigationService;
        private readonly LomentService _lomentService;
        private readonly IUIDispatcher _dispatcher;
        private readonly DataStore _dataStore;
        private readonly IPlatformUtility _platformUtility;
        private DataService _dataAcccess;

        private string _subscriptionStatus = "Subscription: Unknown";
        public string SubscriptionStatus
        {
            get { return _subscriptionStatus; }
            set { Set(ref _subscriptionStatus, value); }
        }

        private RegisterViewModel _registerViewModel;
        public RegisterViewModel RegisterViewModel
        {
            get { return _registerViewModel; }
            set { Set(ref _registerViewModel, value); }
        }

        private string _message = "";
        public string Message
        {
            get { return _message; }
            set { Set(ref _message, value); }
        }

        private string _userName;// = "testuser.X.01@lextech.com";
        public string UserName
        {
            get { return _userName; }
            set
            {
                Set(ref _userName, value);
                Authenticate.RaiseCanExecuteChanged(); RecoverPassword.RaiseCanExecuteChanged();
                if (AccountDatabases?.Any(x => x == value) == true)
                {
                    Task.Run(async () =>
                    {
                        await _dataStore.InitDatabaseTablesAsync(value + ".db");
                        var la = await _dataStore.LomentAccountGetAsync();
                        _dispatcher.BeginInvokeOnMainThread(() =>
                        {
                            Password = la.Password;
                        });
                    });
                }
            }
        }

        private string _password = "";
        public string Password
        {
            get { return _password; }
            set { Set(ref _password, value); Authenticate.RaiseCanExecuteChanged(); }
        }

        public CNLomentAccount AuthenticatedLomentAccount { get; set; }


        private string _cashewAccountToCreate;
        public string CashewAccountToCreate
        {
            get { return _cashewAccountToCreate; }
            set { Set(ref _cashewAccountToCreate, value); CreateCashewAccount.RaiseCanExecuteChanged(); }
        }

        private ObservableCollection<CashewAccountViewModel> _cashewAccounts;
        public ObservableCollection<CashewAccountViewModel> CashewAccounts
        {
            get { return _cashewAccounts; }
            set { Set(ref _cashewAccounts, value); }
        }


        private CashewAccountViewModel _selectedCashewAccount;
        public CashewAccountViewModel SelectedCashewAccount
        {
            get { return _selectedCashewAccount; }
            set { Set(ref _selectedCashewAccount, value); }
        }

        public LomentAccountViewModel(LomentService lomentService, DataStore dataStore, INavigationService navigationService, IUIDispatcher dispatcher, IPlatformUtility platformUtility)
        {
            //  Sdk.Initialize(_apiConfig);

            _dataStore = dataStore;
            _lomentService = lomentService;
            _navigationService = navigationService;
            _dispatcher = dispatcher;
            _platformUtility = platformUtility;
            _registerViewModel = new RegisterViewModel(_lomentService);
        }



        private RelayCommand _authenticate;
        public RelayCommand Authenticate
        {
            get
            {
                return _authenticate ?? (_authenticate = new RelayCommand(ExecAuthenticate, CanAuthenticate));
            }
        }

        /// <summary>
        /// Checks whether the Authenticate command is executable
        /// </summary>
        private bool CanAuthenticate()
        {
            return !string.IsNullOrEmpty(UserName) && !string.IsNullOrEmpty(Password);
        }

        /// <summary>
        /// Executes the Authenticate command
        /// Creates the database connection
        /// Create database tables if needed
        /// </summary>
        private async void ExecAuthenticate()
        {
            try
            {
                AuthenticatedLomentAccount = await _lomentService.AuthenticateAsync(UserName, Password);
            }
            catch (ApiException ex)
            {
                Message = ex.Message;
                return;
            }

            VerifySubscription.RaiseCanExecuteChanged();

            ServiceLocator.SetLocatorProvider(() => SimpleIoc.Default);

            var ioc = SimpleIoc.Default;

            DataStore dataStore = SimpleIoc.Default.GetInstance<DataStore>();

            /* Set up the database - Creates tables if it is a new database
               This uses the name of the authenticated user as the DB name.
               This in turn allows for the app to support multiple Loment accounts. */
            await dataStore.InitDatabaseTablesAsync(AuthenticatedLomentAccount.UserName + ".db");

            DataService.LomentAccountCreateOrUpdate(_dataStore, AuthenticatedLomentAccount);

            await RegisterAndLoad();
            _navigationService.Navigate("/ChatPage.xaml");
        }
        
        private RelayCommand _createCashewAccount;
        public RelayCommand CreateCashewAccount
        {
            get
            {
                return _createCashewAccount ?? (_createCashewAccount = new RelayCommand(ExecCreateCashewAccount, CanCreateCashewAccount));
            }
        }

        /// <summary>
        /// Checks whether the CreateCashewAccount command is executable
        /// </summary>
        private bool CanCreateCashewAccount()
        {
            return AuthenticatedLomentAccount != null && !string.IsNullOrEmpty(CashewAccountToCreate);
        }

        /// <summary>
        /// Executes the CreateCashewAccount command 
        /// </summary>
        private async void ExecCreateCashewAccount()
        {
            try
            {
                var newAccount = await _lomentService.CreateCashewAcountAsync(AuthenticatedLomentAccount, CashewAccountToCreate);

                var dbAccount = await _dataAcccess.CashewAccountCreate(newAccount);

                _dataAcccess.ActivateCashewAccount(dbAccount);

                // build a view model for it
                var newAccountViewModel = new CashewAccountViewModel(_lomentService, _dataStore, _dataAcccess, dbAccount, _dispatcher, _navigationService, _platformUtility);


                // and append since we don't know where to put it - is this list sorted?
                CashewAccounts.Add(newAccountViewModel);
            }

            catch (ApiException)
            {
                // creating the account failed
            }
        }


        private RelayCommand _verifySubscription;
        public RelayCommand VerifySubscription
        {
            get
            {
                return _verifySubscription ?? (_verifySubscription = new RelayCommand(ExecVerifySubscription, CanVerifySubscription));
            }
        }

        /// <summary>
        /// Checks whether the VerifySubscription command is executable
        /// </summary>
        private bool CanVerifySubscription()
        {
            return AuthenticatedLomentAccount != null;
        }

        /// <summary>
        /// Executes the VerifySubscription command 
        /// </summary>
        private async void ExecVerifySubscription()
        {
            try
            {

                await _lomentService.VerifySubscriptionAsync(AuthenticatedLomentAccount);
                SubscriptionStatus = "Subscription: Valid";
            }
            catch (ApiException)
            {

                SubscriptionStatus = "Subscription: InValid";
            }
        }



        private RelayCommand _recoverPassword;
        public RelayCommand RecoverPassword
        {
            get
            {
                return _recoverPassword ?? (_recoverPassword = new RelayCommand(ExecRecoverPassword, CanRecoverPassword));
            }
        }

        /// <summary>
        /// Checks whether the RecoverPassword command is executable
        /// </summary>
        private bool CanRecoverPassword()
        {
            return !string.IsNullOrEmpty(UserName);
        }

        /// <summary>
        /// Executes the RecoverPassword command 
        /// </summary>
        private async void ExecRecoverPassword()
        {
            try
            {
                await _lomentService.RecoverPasswordAsync(UserName);
            }
            catch (ApiException)
            {

            }
        }


        //private RelayCommand _createDevice;
        //public RelayCommand CreateDevice
        //{
        //    get
        //    {
        //        return _createDevice ?? (_createDevice = new RelayCommand(ExecCreateDevice, CanCreateDevice));
        //    }
        //}

        ///// <summary>
        ///// Checks whether the CreateDevice command is executable
        ///// </summary>
        //private bool CanCreateDevice()
        //{
        //    return AuthenticatedUser != null;
        //}

        /// <summary>
        /// Executes the CreateDevice command 
        /// </summary>
        private async Task RegisterAndLoad()
        {
#if DEBUG
            SimpleIoc.Default.GetInstance<IDeviceInformationProvider>().FakeDeviceIDSuffix = AuthenticatedLomentAccount.UserName.Split("@".ToCharArray())[0].Replace(".", "");
#endif
            try
            {
                await _lomentService.CreateDeviceAsync(AuthenticatedLomentAccount);

            }
            catch (ApiException ae)
            {
                if (ae.ErrorCode == 20102)
                {
                    //ignore
                }
                else
                    throw;
            }

            //   await _lomentService.VerifySubscriptionAsync(AuthenticatedLomentAccount);

            if (SimpleIoc.Default.IsRegistered<DataService>())
                SimpleIoc.Default.Unregister<DataService>();

            SimpleIoc.Default.Register<DataService>(() => new DataService(_lomentService, _dataStore));
            _dataAcccess = SimpleIoc.Default.GetInstance<DataService>();
            await _dataAcccess.Start(AuthenticatedLomentAccount);

            var dbAccounts = await _dataStore.CashewAccountsGetAllListAsync();

            CashewAccounts = new ObservableCollection<CashewAccountViewModel>(
               dbAccounts.Select(x => new CashewAccountViewModel(_lomentService, _dataStore, _dataAcccess, x, _dispatcher, _navigationService, _platformUtility)));
        }

        private RelayCommand _goToSetting;

        /// <summary>
        /// Navigate to the Settings page
        /// </summary>
        public RelayCommand GoToSettings
        {
            get
            {
                return _goToSetting ?? (_goToSetting = new RelayCommand(ExecGoToSettings, CanGoToSettings));
            }
        }

        /// <summary>
        /// Checks whether the GoToSettings command is executable
        /// </summary>
        private bool CanGoToSettings()
        {
            return true;
        }

        /// <summary>
        /// Executes the GoToSettings command 
        /// </summary>
        private void ExecGoToSettings()
        {
            _navigationService.Navigate("/SettingsPage.xaml");
        }



        private ObservableCollection<string> _accountDatabases;
        public ObservableCollection<string> AccountDatabases
        {
            get { return _accountDatabases; }
            set { Set(ref _accountDatabases, value); }
        }

        private RelayCommand _load;
        public RelayCommand Load
        {
            get
            {
                return _load ?? (_load = new RelayCommand(ExecLoad, CanLoad));
            }
        }

        /// <summary>
        /// Checks whether the Load command is executable
        /// </summary>
        private bool CanLoad()
        {
            return true;
        }

        /// <summary>
        /// Executes the Load command 
        /// </summary>
        private void ExecLoad()
        {
            var dbs = _dataStore.GetAvailbleAccountDatabases();
            for (int i = 0; i < dbs.Count; i++)
            {
                if (dbs[i].EndsWith(".db"))
                {
                    dbs[i] = dbs[i].Substring(0, dbs[i].Length - 3);
                }
            }
            AccountDatabases = new ObservableCollection<string>(dbs);
        }

    }
}
