using Core.Services;
using GalaSoft.MvvmLight;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;

namespace WpfCashew.Services
{
    public class WpfNavigationService : INavigationService
    {
        Frame _frame;
        Button _backButton;

        public WpfNavigationService(Frame frame, Button backButton)
        {
            _frame = frame;
            _backButton = backButton;
            _backButton.Click += _backButton_Click;
            _frame.Navigated += _frame_Navigated;
        }

        private void _frame_Navigated(object sender, System.Windows.Navigation.NavigationEventArgs e)
        {
            if(_dataContextForTargetPage != null)
            {
                (e.Content as FrameworkElement).DataContext = _dataContextForTargetPage;
                _dataContextForTargetPage = null;
            }
            _backButton.IsEnabled = _frame.CanGoBack;
        }

        private void _backButton_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            GoBack();
        }

        public void GoBack()
        {
            if (_frame.CanGoBack)
                _frame.GoBack();
          //  _backButton.IsEnabled = _frame.CanGoBack;
        }

        ViewModelBase _dataContextForTargetPage; 
        public void Navigate(string uri, ViewModelBase vm = null)
        {
            _dataContextForTargetPage = vm;
            _frame.Navigate(new Uri(uri, UriKind.Relative));
 
        }
    }
}
