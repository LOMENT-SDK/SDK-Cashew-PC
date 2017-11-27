using Cashew;
using Core.Services;
using Core.ViewModel;

using GalaSoft.MvvmLight.Ioc;
using MahApps.Metro.Controls;
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
using WpfCashew.Services;

namespace WpfCashew
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : MetroWindow
    {

        public void ShowBackButton()
        {
            backButton.Visibility = Visibility.Visible;
        }

        public void HideBackButton()
        {
            backButton.Visibility = Visibility.Collapsed;
        }

        public MainWindow()
        {

            SimpleIoc.Default.Register<INavigationService>(() => new WpfNavigationService(mainFrame, backButton));
            InitializeComponent();

            Closed += MainWindow_Closed;
        }

        private void MainWindow_Closed(object sender, EventArgs e)
        {
            App.Current.Shutdown(0);
        }
    }
}
