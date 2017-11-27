using Cashew.Internal.Loment.Dto;
using System;

namespace Cashew
{
    /// <summary>
    /// Represents exception possibly thrown by the Loment API.
    /// </summary>
    public class ApiException : Exception
    {
        /// <summary>
        /// An error code from the server.
        /// </summary>
        public int ErrorCode { get; private set; }

        internal ApiException(ResponseMeta response) 
            : base(response.Message)
        {
            ErrorCode = response.Code;
        }

        internal ApiException(ResponseMeta response, string extraInfo)
           : base(response.Message + Environment.NewLine + extraInfo)
        {
            ErrorCode = response.Code;
        }

    }
}
