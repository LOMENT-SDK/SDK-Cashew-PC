using Core.ViewModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;

namespace WpfCashew
{
    public class ConversationTemplateSelector : DataTemplateSelector
    {
        public override DataTemplate SelectTemplate(object item, DependencyObject container)
        {
            FrameworkElement element = container as FrameworkElement;

            if (element != null && item != null && item is ConversationViewModel)
            {
                ConversationViewModel vm = item as ConversationViewModel;

                if (vm.IsGroup)
                {
                    return element.FindResource("groupConversationTemplate") as DataTemplate;
                }
                else
                {
                    return element.FindResource("directConversationTemplate") as DataTemplate;
                }

            }

            return null;
        }
    }
}
