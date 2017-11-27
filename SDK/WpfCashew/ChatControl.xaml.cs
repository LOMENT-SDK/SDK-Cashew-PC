using Cashew.Model;
using Core.ViewModel;
using Microsoft.Win32;
using System;
using System.Collections.Generic;
using System.IO;
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
    /// Interaction logic for ChatControl.xaml
    /// </summary>
    public partial class ChatControl : UserControl
    {
        public CashewAccountViewModel ViewModel { get { return DataContext as CashewAccountViewModel; } }
        public ChatControl()
        {
            InitializeComponent();

            DataContextChanged += ChatControl_DataContextChanged;

            listBox.DataContextChanged += ListBox_DataContextChanged;
        }

        private void ListBox_DataContextChanged(object sender, DependencyPropertyChangedEventArgs e)
        {
            if (e.OldValue != null)
            {
                (e.OldValue as ConversationViewModel).Messages.CollectionChanged -= Messages_CollectionChanged;
            }
            if (e.NewValue != null)
            {
                (e.NewValue as ConversationViewModel).Messages.CollectionChanged += Messages_CollectionChanged;
            }
        }

        private void Messages_CollectionChanged(object sender, System.Collections.Specialized.NotifyCollectionChangedEventArgs e)
        {
            if (VisualTreeHelper.GetChildrenCount(listBox) > 0)
            {
                Border border = (Border)VisualTreeHelper.GetChild(listBox, 0);
                ScrollViewer scrollViewer = (ScrollViewer)VisualTreeHelper.GetChild(border, 0);
                scrollViewer.ScrollToBottom();
            }
        }


        private void ChatControl_DataContextChanged(object sender, DependencyPropertyChangedEventArgs e)
        {
            (e.NewValue as CashewAccountViewModel).Load.Execute(null);
        }

        private void Button_Click(object sender, RoutedEventArgs e)
        {
            OpenFileDialog of = new OpenFileDialog();
            of.Multiselect = true;
            of.CheckFileExists = true;

            if (of.ShowDialog() == true)
            {

                foreach (var item in of.FileNames)
                {
                    FileInfo fi = new FileInfo(item);
                    CNAttachmentData ad = new CNAttachmentData();
                    ad.Name = fi.Name;
                    ad.Data = File.ReadAllBytes(item);
                    ViewModel.SelectedConversation.Compose.Attachments.Add(ad);
                }
            }

        }
    }
}
