using GalaSoft.MvvmLight;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Core.Services
{
    public interface INavigationService
    {
        void Navigate(string uri, ViewModelBase viewModel = null);

        void GoBack();
    }
}
