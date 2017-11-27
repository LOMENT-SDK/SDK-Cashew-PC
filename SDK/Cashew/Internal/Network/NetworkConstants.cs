using System;

namespace Cashew.Internal.Network
{
    internal static class NetworkConstants
    {
#if TESTSERVER
        internal static TimeSpan SERVER_TIME_DRIFT = new TimeSpan(0, 7, 36);
#else
        internal static TimeSpan SERVER_TIME_DRIFT = new TimeSpan(0); 
#endif

#if DEBUG
        internal const int QueueDefaultTimeOut = int.MaxValue;
#else
        internal const int QueueDefaultTimeOut = 20000;
#endif

        // TODO: needs to be dynamic
        internal const string PACKAGE_NAME = "com.loment.cashew.sdk";

        internal const string HTTP_DIGEST_USER = @"info@loment.net";
        internal const string HTTP_DIGEST_PASSWORD = @"c85e22e3fa2ba99c425db4bef6cb1a21431ab6f6";
#if TESTSERVER
        internal const string HTTP_DIGEST_REALM = @"api-sthithi.loment.net";
#else
        internal const string HTTP_DIGEST_REALM = @"api-sthithi.loment.net";
#endif

#if TESTSERVER
        internal const string BASE_API_URL = @"https://api-sthithi.loment.net:443";
#else
        internal const string BASE_API_URL = @"https://api-sthithi.loment.net:443";
#endif

#if TESTSERVER
        internal const string HTTP_ATTACHMENT_SERVER_URL = @"https://attachcashew.loment.net:443";
#else
        internal const string HTTP_ATTACHMENT_SERVER_URL = @"https://attachcashew.loment.net:443";
#endif

#if TESTSERVER
        internal const string HTTP_ATTACHMENT_DIGEST_REALM = @"Restricted area";
#else
        internal const string HTTP_ATTACHMENT_DIGEST_REALM = @"Restricted area";
#endif

        internal const string HTTP_UPLOAD_DIGEST_USER = @"cashew_upload";
        internal const string HTTP_UPLOAD_DIGEST_PASSWORD = @"efgmn245tmnewfmn245g";
        internal const string HTTP_DOWNLOAD_DIGEST_USER = @"cashew_download";
        internal const string HTTP_DOWNLOAD_DIGEST_PASSWORD = @"efgmn245tmnewfmn245g";

        internal const int FEATURE_ID = 3;


#if TESTSERVER
        internal const string AMQP_REQUEST_QUEUE_HOSTNAME = "amqpreq.cashew.loment.net";
#else
        internal const string AMQP_REQUEST_QUEUE_HOSTNAME = "amqpreq.cashew.loment.net";
#endif
        internal const string AMQP_REQUEST_QUEUE_V_HOST = "cashew_requests";
        internal const string AMQP_REQUEST_QUEUE_USERNAME = "req_writer";
        internal const string AMQP_REQUEST_QUEUE_PASSWORD = "r3q_Wr1t3r@312";
        internal const string AMQP_REQUEST_QUEUE_EXCHANGE = "";
        internal const string AMQP_REQUEST_QUEUE_BINDING_KEY = "";
        internal const string AMQP_REQUEST_QUEUE_ROUTING_KEY = "rpc_queue";
        internal const int AMQP_REQUEST_QUEUE_PORT = 5672;


#if TESTSERVER
        internal const string AMQP_RESPONSE_QUEUE_HOSTNAME = "amqpresp.cashew.loment.net";
#else
        internal const string AMQP_RESPONSE_QUEUE_HOSTNAME = "amqpresp.cashew.loment.net";
#endif
        internal const string AMQP_RESPONSE_QUEUE_V_HOST = "cashew_responses";
        internal const string AMQP_RESPONSE_QUEUE_USERNAME = "resp_reader";
        internal const string AMQP_RESPONSE_QUEUE_PASSWORD = "r35p_R3@d3r@312";
        internal const int AMQP_RESPONSE_QUEUE_PORT = 5672;


#if TESTSERVER
        internal const string AMQP_MESSAGE_QUEUE_HOSTNAME = "amqpmsg.cashew.loment.net";
#else
        internal const string AMQP_MESSAGE_QUEUE_HOSTNAME = "amqpmsg.cashew.loment.net";
#endif
        internal const string AMQP_MESSAGE_QUEUE_V_HOST = "cashew_messages";
        internal const string AMQP_MESSAGE_QUEUE_USERNAME = "msg_reader";
        internal const string AMQP_MESSAGE_QUEUE_PASSWORD = "w5g_R3@d3r@312";
        internal const int AMQP_MESSAGE_QUEUE_PORT = 5672;
    }
}
