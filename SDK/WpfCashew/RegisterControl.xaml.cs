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
    /// Interaction logic for RegisterControl.xaml
    /// </summary>
    public partial class RegisterControl : UserControl
    {
        public RegisterControl()
        {
            InitializeComponent();

            this.Loaded += RegisterControl_Loaded;
            this.Unloaded += RegisterControl_Unloaded;
        }

        private void RegisterControl_Unloaded(object sender, RoutedEventArgs e)
        {
            (App.Current.MainWindow as MainWindow).HideBackButton();
        }

        private void RegisterControl_Loaded(object sender, RoutedEventArgs e)
        {
            (App.Current.MainWindow as MainWindow).ShowBackButton();
        }
    }
}
