/*
  In App.xaml:
  <Application.Resources>
      <vm:ViewModelLocator xmlns:vm="clr-namespace:WpfCashew"
                           x:Key="Locator" />
  </Application.Resources>
  
  In the View:
  DataContext="{Binding Source={StaticResource Locator}, Path=ViewModelName}"

  You can also use Blend to do all this with the tool's support.
  See http://www.galasoft.ch/mvvm
*/

using Cashew;
using GalaSoft.MvvmLight;
using GalaSoft.MvvmLight.Ioc;
using Microsoft.Practices.ServiceLocation;

namespace Core.ViewModel
{
    /// <summary>
    /// This class contains static references to all the view models in the
    /// application and provides an entry point for the bindings.
    /// </summary>
    public class ViewModelLocator
    {
        /// <summary>
        /// Initializes a new instance of the ViewModelLocator class.
        /// </summary>
        public ViewModelLocator()
        {
            ServiceLocator.SetLocatorProvider(() => SimpleIoc.Default);

            if (SimpleIoc.Default.IsRegistered<LomentService>() == false)
            {
                SimpleIoc.Default.Register<LomentService>(() =>
                {
                    return new LomentService("SDKC001", "1");
                });

                SimpleIoc.Default.Register<MessagingService>();

                SimpleIoc.Default.Register<MainViewModel>();

                SimpleIoc.Default.Register<LomentAccountViewModel>();

                SimpleIoc.Default.Register<FriendsViewModel>();
            }
        }

        public MainViewModel Main
        {
            get
            {
                return ServiceLocator.Current.GetInstance<MainViewModel>();
            }
        }

        public LomentAccountViewModel LomentAccount
        {
            get
            {
                return ServiceLocator.Current.GetInstance<LomentAccountViewModel>();
            }
        }


#if DEBUG
        // For design time only

        public FriendsViewModel Friends
        {
            get
            {
                return ServiceLocator.Current.GetInstance<FriendsViewModel>();
            }
        }

        public CashewAccountViewModel CashewAccount
        {
            get
            {
                return ServiceLocator.Current.GetInstance<CashewAccountViewModel>();
            }
        }
#endif

        public static void Cleanup()
        {
            // TODO Clear the ViewModels
        }
    }
}