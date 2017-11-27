namespace Cashew.Internal.Loment.Dto
{
    internal class SubscriptionDto : ResponseDtoBase
    {
        public SubscriptionDetailsDto subscription_details { get; set; }

        public object createNew { get; set; }
    }
}
