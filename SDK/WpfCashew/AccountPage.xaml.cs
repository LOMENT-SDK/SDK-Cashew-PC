using Core.Services;
using Core.ViewModel;
using GalaSoft.MvvmLight.Ioc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace WpfCashew
{
    /// <summary>
    /// Interaction logic for AccountPage.xaml
    /// </summary>
    public partial class AccountPage : Page
    {
        public LomentAccountViewModel ViewModel { get { return DataContext as LomentAccountViewModel; } }
        public AccountPage()
        {
            InitializeComponent();

            passworBox.Password = ViewModel.Password;
            ViewModel.PropertyChanged += ViewModel_PropertyChanged;
#if YDEVICE
            ViewModel.UserName = "testuser.Y.01@lextech.com";
#endif
        }

        private void ViewModel_PropertyChanged(object sender, System.ComponentModel.PropertyChangedEventArgs e)
        {
            if (e.PropertyName == "Password")
            {
                passworBox.PasswordChanged -= OnPasswordChanged;
                passworBox.Password = ViewModel.Password;
                passworBox.PasswordChanged += OnPasswordChanged;
            }
        }



        private void OnPasswordChanged(object sender, RoutedEventArgs e)
        {
            ViewModel.PropertyChanged -= ViewModel_PropertyChanged;
            ViewModel.Password = passworBox.Password;
            ViewModel.PropertyChanged += ViewModel_PropertyChanged;
        }

        private void OnRegister(object sender, RoutedEventArgs e)
        {

            SimpleIoc.Default.GetInstance<INavigationService>().Navigate("/RegisterControl.xaml");
        }
    }
}
