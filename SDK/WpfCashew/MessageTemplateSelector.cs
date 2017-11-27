using Cashew.Model;
using Core.ViewModel;
using Loment.CashewReference.DB.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;

namespace WpfCashew
{
    public class MessageTemplateSelector : DataTemplateSelector
    {
        public override DataTemplate SelectTemplate(object item, DependencyObject container)
        {
            FrameworkElement element = container as FrameworkElement;

            if (element != null && item != null && item is MessageViewModel)
            {
                DbMessage msg = (item as MessageViewModel).Message;

                if (msg.IsFriendRequest == false)
                {
                    if (msg.Direction == MessageDirection.Incoming)
                    {
                        if (msg.IsMarkedAsDeleted)
                            return element.FindResource("deletedIncomingMessageTemplate") as DataTemplate;
                        else
                            return element.FindResource("incomingMessageTemplate") as DataTemplate;
                    }
                    else
                    {
                        if (msg.IsMarkedAsDeleted)
                            return element.FindResource("deletedOutgoingMessageTemplate") as DataTemplate;
                        else
                            return element.FindResource("outgoingMessageTemplate") as DataTemplate;
                    }
                }
                else
                {
                    switch (msg.FriendRequestStatus)
                    {
                        case FriendRequestStatus.Sent:
                            if (msg.Direction == MessageDirection.Incoming)
                            {
                                return element.FindResource("incomingFriendRequestTemplate") as DataTemplate;
                            }
                            else
                            {
                                return element.FindResource("outgoingFriendRequestTemplate") as DataTemplate;
                            }
                        case FriendRequestStatus.Accepted:
                            return element.FindResource("acceptedFriendRequestTemplate") as DataTemplate;
                        case FriendRequestStatus.Rejected:
                            return element.FindResource("rejectedFriendRequestTemplate") as DataTemplate;
                        default:
                            return null;
                    }

                }
            }

            return null;
        }
    }
}
