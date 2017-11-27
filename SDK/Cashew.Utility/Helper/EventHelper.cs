using System;

namespace Cashew.Utility.Helper
{

    /// <summary>
    /// A helper class to safely invoke events.
    /// </summary>
    public static class EventHelper
    {
        public static void NotifyEvent(object sender, EventHandler eventHandler)
        {
            EventHandler eventHandlerSafeCopy = eventHandler;

            if (eventHandlerSafeCopy != null)
            {
                eventHandlerSafeCopy.Invoke(sender, EventArgs.Empty);
            }
        }

        public static void NotifyEvent<T>(object sender, EventHandler<T> eventHandler, T args)
        {
            EventHandler<T> eventHandlerSafeCopy = eventHandler;
            if (eventHandlerSafeCopy != null)
            {
                eventHandlerSafeCopy.Invoke(sender, args);
            }
        }
    }
}
