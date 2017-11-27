namespace Cashew.Model.NetworkResponses
{
    /// <summary>
    /// A response info class carrying error codes and comment for request send to the Cashew back-end.
    /// </summary>
    public class CashewResponse
    {
        /// <summary>
        /// The error code send back from the server.
        /// </summary>
        public int ResponseCode { get; private set; }

        /// <summary>
        /// A comment regarding the success or failure of the operation.
        /// </summary>
        public string Comment { get; private set; }

        /// <summary>
        /// INdicates whether the call was a success.
        /// </summary>
        public bool IsSuccess { get; private set; }

        internal CashewResponse(int code, string comment, bool isSuccess)
        {
            ResponseCode = code;
            Comment = comment;
            IsSuccess = isSuccess;
        }
    }

    /// <summary>
    /// A generic version of the CashewResponse class that in addition carries a generic entity of a result.
    /// </summary>
    /// <typeparam name="T"></typeparam>
    public class CashewResponse<T> : CashewResponse
    {
        /// <summary>
        /// The payload of the response.
        /// </summary>
        public T Entity { get; private set; }

        public CashewResponse(int code, string comment, bool isSuccess, T response)
            : base(code, comment, isSuccess)
        {
            Entity = response;
        }
    }
}
