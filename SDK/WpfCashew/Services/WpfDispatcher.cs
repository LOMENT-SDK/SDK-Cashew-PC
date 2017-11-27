using Core.Services;
using GalaSoft.MvvmLight.Threading;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WpfCashew.Services
{
     public class WpfDispatcher : IUIDispatcher
    {
        public void BeginInvokeOnMainThread(Action action)
        {
            DispatcherHelper.CheckBeginInvokeOnUI(action);
        }
    }
}
