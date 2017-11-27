using System;

namespace Core.Services
{
    public interface IUIDispatcher
    {
        void BeginInvokeOnMainThread(Action action);
    }
}