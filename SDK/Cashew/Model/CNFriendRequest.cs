namespace Cashew.Model
{
    /// <summary>
    /// A message class that represents a friend request.
    /// </summary>
    public class CNFriendRequest : CNMessageBase
    {
        public CNFriendStatus Status { get; set; }
    }
}
