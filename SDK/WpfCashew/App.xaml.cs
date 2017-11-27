using Cashew;
using Cashew.Services;
using Core.Services;
using GalaSoft.MvvmLight.Ioc;
using GalaSoft.MvvmLight.Threading;
using Loment.CashewReference.DB.Services;
using Microsoft.Practices.ServiceLocation;
using RabbitMQNet;
using System.Windows;
using WpfCashew.Services;
using Cashew.Utility.Services;
using Loment.CashewReference.DB.Services.Interfaces;
using Win32DeviceProvider;

namespace WpfCashew
{


    /// <summary>
    /// Interaction logic for App.xaml
    /// </summary>
    public partial class App : Application
    {
        /// <summary>
        /// Set things up!
        /// </summary>
        /// <param name="e"></param>
        protected override void OnStartup(StartupEventArgs e)
        {
            base.OnStartup(e);

            SimpleIoc.Default.Register<IDeviceInformationProvider, Win32DeviceInformationProvider>();
            Sdk.Initialize(SimpleIoc.Default.GetInstance<IDeviceInformationProvider>(), new NetRabbitMQService());

            RegisterServices();
            DoSetup();

        }

        /// <summary>
        /// Register the IOC services required for dependency injection
        /// </summary>
        private void RegisterServices()
        {
            ServiceLocator.SetLocatorProvider(() => SimpleIoc.Default);

            DispatcherHelper.Initialize();

            var ioc = SimpleIoc.Default;
            ioc.Register<ISqLitePcl, WpfSqlitePcl>();
            ioc.Register<IPlatformUtility, WpfPlatformUtility>();
            ioc.Register<DataStore>();

            ioc.Register<IUIDispatcher, WpfDispatcher>();

        }

        /// <summary>
        /// Set up the PlatformUtility Service
        /// </summary>
        private void DoSetup()
        {
            IPlatformUtility iPlatformUtility = SimpleIoc.Default.GetInstance<IPlatformUtility>();
            iPlatformUtility.Init();
        }

    }
}
