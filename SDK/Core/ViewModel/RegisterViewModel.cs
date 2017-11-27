using Cashew;
using GalaSoft.MvvmLight;
using GalaSoft.MvvmLight.Command;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Core.ViewModel
{
    public class RegisterViewModel : ViewModelBase
    {
        private string _message;
        public string Message
        {
            get { return _message; }
            set { Set(ref _message, value); }
        }

        private string _name;
        public string Name
        {
            get { return _name; }
            set { Set(ref _name, value); Register.RaiseCanExecuteChanged(); }
        }

        private string _email;
        public string Email
        {
            get { return _email; }
            set { Set(ref _email, value); Register.RaiseCanExecuteChanged(); }
        }

        private string _phone;
        public string Phone
        {
            get { return _phone; }
            set { Set(ref _phone, value); Register.RaiseCanExecuteChanged(); }
        }

        private string _countryCode;
        public string CountryCode
        {
            get { return _countryCode; }
            set { Set(ref _countryCode, value); Register.RaiseCanExecuteChanged(); }
        }

        private string _password;
        public string Password
        {
            get { return _password; }
            set { Set(ref _password, value); Register.RaiseCanExecuteChanged(); }
        }

        LomentService _lomentAccountManger;
        public RegisterViewModel(LomentService lomentService)
        {
            _lomentAccountManger = lomentService;
        }

        private RelayCommand _register;
        public RelayCommand Register
        {
            get
            {
                return _register ?? (_register = new RelayCommand(ExecRegister, CanRegister));
            }
        }

        /// <summary>
        /// Checks whether the Register command is executable
        /// </summary>
        private bool CanRegister()
        {
            return !string.IsNullOrEmpty(Name)
                && !string.IsNullOrEmpty(Email)
                && !string.IsNullOrEmpty(Password)
                && !string.IsNullOrEmpty(Phone)
                && !string.IsNullOrEmpty(CountryCode) 
                && CountryCode.Length == 2;
        }

        /// <summary>
        /// Executes the Register command 
        /// </summary>
        private async void ExecRegister()
        {
            try
            {
                Message = "";

                await _lomentAccountManger.RegisterAsync(Name, Password, Email, Phone, CountryCode);
                Name = "";
                Password = "";
                Phone = "";
                CountryCode = "";
                Email = "";

                Message = "Success";
            }
            catch (ApiException lae)
            {
                Message = "Failed: " + lae.Message;
            }
        }

    }
}
