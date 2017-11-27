using Cashew;
using Core.Services;
using GalaSoft.MvvmLight;
using GalaSoft.MvvmLight.Command;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Core.ViewModel
{
    public class GroupEditViewModel : ViewModelBase
    {


        private MessagingService _messagingService;
        private INavigationService _navService;

        private string _groupId;

        private bool _isNewGroup;
        public bool IsNewGroup
        {
            get { return _isNewGroup; }
            set { Set(ref _isNewGroup, value); }
        }

        private string _originalName;
        private string _name;
        public string Name
        {
            get { return _name; }
            set { Set(ref _name, value); }
        }

        private List<string> _originalMembers;
        private ObservableCollection<string> _members;
        public ObservableCollection<string> Members
        {
            get { return _members; }
            set { Set(ref _members, value); }
        }

        private string _originalOwner;
        private string _owner;
        public string Owner
        {
            get { return _owner; }
            set { Set(ref _owner, value); }
        }

        private string _selectedContact;
        public string SelectedContact
        {
            get { return _selectedContact; }
            set { Set(ref _selectedContact, value); AddSelectedContact.RaiseCanExecuteChanged(); }
        }

        private string _selectedMember;
        public string SelectedMember
        {
            get { return _selectedMember; }
            set { Set(ref _selectedMember, value); RemoveSelectedMember.RaiseCanExecuteChanged(); }
        }

        private ObservableCollection<string> _contacts;
        public ObservableCollection<string> Contacts
        {
            get { return _contacts; }
            set { Set(ref _contacts, value); }
        }

        public GroupEditViewModel(string groupId, List<string> knownContacts, MessagingService messagingService, INavigationService navService)
        {
            _groupId = groupId;


            IsNewGroup = string.IsNullOrEmpty(_groupId);
            _navService = navService;
            _messagingService = messagingService;
            Members = new ObservableCollection<string>();

            if (!IsNewGroup)
                LoadGroup(knownContacts);
            else
                Contacts = new ObservableCollection<string>(knownContacts);


        }

        private async void LoadGroup(List<string> knownContacts)
        {
            var resp = await _messagingService.GetGroupDetailsAsync(_groupId);
            if (resp.IsSuccess)
            {
                Name = resp.Entity.Name;
                _originalName = resp.Entity.Name;
                Members = new ObservableCollection<string>(resp.Entity.Member);
                _originalMembers = Members.ToList();
                _originalOwner = resp.Entity.Owner;
                Owner = resp.Entity.Owner;
            }
            Contacts = new ObservableCollection<string>(knownContacts.Where(x => Members.Any(y => y == x) == false));
        }

        private RelayCommand _save;
        public RelayCommand Save
        {
            get
            {
                return _save ?? (_save = new RelayCommand(ExecSave, CanSave));
            }
        }

        /// <summary>
        /// Checks whether the Save command is executable
        /// </summary>
        private bool CanSave()
        {
            return true;
        }

        /// <summary>
        /// Executes the Save command 
        /// </summary>
        private async void ExecSave()
        {
            if (_isNewGroup)
            {
                var response = await _messagingService.CreateGroupAsync(Name, _members.ToList());
                if (response.IsSuccess)
                {
                    await _messagingService.SendGroupMessageAsync($"Welcome to {response.Entity.Name}", response.Entity.ConverstaionId, new Cashew.Model.CNMessageOptions());
                }
            }
            else
            {
                if (_originalName != Name)
                {
                    var response = await _messagingService.EditGroupNameAsync(_groupId, Name);
                }

                if (_originalOwner != Owner)
                {
                    var response = await _messagingService.ChangeGroupOwnerAsync(_groupId, Owner);
                }

                var toAdd = _members.Where(x => _originalMembers.Any(y => y == x) == false).ToList();
                if (toAdd.Any())
                {
                    var response = await _messagingService.AddMembersToGroupAsync(_groupId, toAdd);
                }

                var toRemove = _originalMembers.Where(x => Members.Any(y => y == x) == false).ToList();
                if (toRemove.Any())
                {
                    var response = await _messagingService.RemoveMembersFromGroupAsync(_groupId, toRemove);
                }
            }
        }


        private RelayCommand _addSelectedContact;
        public RelayCommand AddSelectedContact
        {
            get
            {
                return _addSelectedContact ?? (_addSelectedContact = new RelayCommand(ExecAddSelectedContact, CanAddSelectedContact));
            }
        }

        /// <summary>
        /// Checks whether the AddSelectedContact command is executable
        /// </summary>
        private bool CanAddSelectedContact()
        {
            return !string.IsNullOrEmpty(SelectedContact);
        }

        /// <summary>
        /// Executes the AddSelectedContact command 
        /// </summary>
        private void ExecAddSelectedContact()
        {
            Members.Add(SelectedContact);
            Contacts.Remove(SelectedContact);
            SelectedContact = null;
        }


        private RelayCommand _removeSelectedMember;
        public RelayCommand RemoveSelectedMember
        {
            get
            {
                return _removeSelectedMember ?? (_removeSelectedMember = new RelayCommand(ExecRemoveSelectedMember, CanRemoveSelectedMember));
            }
        }

        /// <summary>
        /// Checks whether the RemoveSelectedMember command is executable
        /// </summary>
        private bool CanRemoveSelectedMember()
        {
            return !string.IsNullOrEmpty(SelectedMember);
        }

        /// <summary>
        /// Executes the RemoveSelectedMember command 
        /// </summary>
        private void ExecRemoveSelectedMember()
        {
            Contacts.Add(SelectedMember);
            Members.Remove(SelectedMember);
            SelectedMember = null;
        }

    }
}
