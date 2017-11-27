using Cashew.Model;
using System;

namespace Cashew.Internal.Network
{
    internal static class ResponseBroker
    {
        internal static event EventHandler<CNFriendSuggestion> FriendRequestResponded;

        internal static void RaiseFriendRequestResponded(CNFriendSuggestion contact, object sender)
        {
            Notify<CNFriendSuggestion>(FriendRequestResponded, contact, sender);
        }

        private static void Notify<T>(EventHandler<T> eventHandler, T args,  object sender)
        {
            var safeCopy = eventHandler;
            if(safeCopy != null)
            {
                eventHandler(sender, args);
            }
        }

        private static void Notify(EventHandler eventHandler, object sender)
        {
            var safeCopy = eventHandler;
            if (safeCopy != null)
            {
                eventHandler(sender, null);
            }
        }
    }
}
