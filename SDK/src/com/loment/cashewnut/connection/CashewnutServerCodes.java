//package com.loment.cashewnut.connection;
//
//public class CashewnutServerCodes {
//	public static void responseStatusFromServer(int status) {
//		switch (status) {
//		case 0:
//			// Successful execution of request
//			break;
//
//		case 1:
//			// Generic server failure
//			break;
//
//		case 2:
//			// Invalid Command
//			break;
//
//		case 3:
//			// Command not allowed without authentication. Authenticate first
//			break;
//
//		case 4:
//			// Invalid message id. Message id not found in database.
//			break;
//
//		case 5:
//			// Authentication already completed. Cannot authenticate again.
//			break;
//
//		case 10001:
//			// Invalid authentication data sent (wrong parameters)/
//			// authentication failed
//			break;
//
//		case 20001:
//			// Invalid header data sent (incorrect/ missing required parameters)
//			break;
//
//		case 20002:
//			// Invalid from id in header
//			break;
//
//		case 20003:
//			// Invalid recipient ids in header
//			break;
//
//		case 20004:
//			// Invalid message type in header
//			break;
//
//		case 20005:
//			// Invalid Priority fag in header
//			break;
//
//		case 20006:
//			// Invalid Restricted fag in header
//			break;
//
//		case 20007:
//			// Invalid acknowledgement fag in header
//			break;
//
//		case 20008:
//			// Invalid expiry fag in header
//			break;
//
//		case 20009:
//			// Invalid subject in header � required parameters missing
//			break;
//
//		case 20010:
//			// Invalid subject in header� mismatch between reported length and
//			// actual data sent
//			break;
//
//		case 20011:
//			// Invalid attachments parameter in header
//			break;
//
//		case 20012:
//			// Invalid attachments count
//			break;
//
//		case 20013:
//			// Invalid attachment header data
//			break;
//
//		case 20014:
//			// Invalid attachments � mismatch between reported count and actual
//			// number attachments reported
//			break;
//
//		case 20101:
//			// Invalid body part � required parameters missing
//			break;
//
//		case 20102:
//			// Body part has already been sent. Cannot be sent again.
//			break;
//
//		case 20201:
//			// Invalid attachment part � required parameters missing
//			break;
//
//		case 20202:
//			// Invalid attachment part � mismatch with header data
//			break;
//
//		case 20203:
//			// Said attachment has already been sent. Cannot be sent again.
//			break;
//
//		case 30001:
//			// Invalid data sent � parameters missing
//			break;
//
//		case 30101:
//			// Invalid data sent � parameters missing/ unknown parameters
//			break;
//
//		case 30102:
//			// Invalid request � requested attachment not found
//			break;
//
//		case 30201:
//			// Invalid request � parameters missing
//			break;
//
//		case 30202:
//			// Setting of folder fag not allowed for this cashewnut_id for this
//			break;
//
//		case 30203:
//			// Setting of read fag not allowed for this cashewnut_id for this
//			// message/ invalid read fag value
//			break;
//
//		case 30204:
//			// Setting of ack fag not allowed for this cashewnut_id for this
//			// message/ invalid ack fag value
//			break;
//
//		case 30205:
//			// Setting of self_deleted fag not allowed for this cashewnut_id for
//			// this message/ invalid self_deleted fag value
//			break;
//
//		case 30206:
//			// Setting of recipient_deleted fag not allowed for this
//			// cashewnut_id for this message/ invalid recipient_deleted fag
//			// value
//			break;
//		}
//
//	}
//}
