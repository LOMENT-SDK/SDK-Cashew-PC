package com.loment.cashewnut.I18N;

import java.util.Locale;

public class AppConfigurationStrings {

	public I18NResources i18NResources = null;

	public AppConfigurationStrings(String type, Locale locale) {
		i18NResources = new I18NResources(type, locale);
		setStringsAqua();
	}

	public void setI18NResources(String type, Locale locale) {
		i18NResources = new I18NResources(type, locale);
		setStringsAqua();
	}

	// //<!-- Company name for Aqua -->
	public String app_name = "";
	public String app_name_bold = "";
	public String btn_signup = "";
	public String et_password = "";
	public String login_progress_signing_in = "";
	public String forgot_password = "";
	public String compose_type_message = "";
	public String dlg_ok = "";
	public String dlg_cancel = "";
	public String dialog_priority_indicator = "";
	public String dialog_expiry_in = "";
	public String forward_backup_restricted = "";

	public String dialog_read_ack = "";
	public String dialog_schedule_message = "";
	public String dialog_mins = "";
	public String dialog_done = "";
	public String contacts_new = "";

	public String error_field_required = "";
	public String yes = "";
	public String no = "";
	public String ok = "";
	public String attachment_size_exceeds = "";
	public String download_attachment_first = "";
	public String add_member = "";
	// <!-- Splash -->
	public String splash_loading = "";

	// <!-- Sign In -->
	public String btn_sign = "";
	public String need_an_account = "";
	public String et_username1 = "";
	public String signin_enter_lomentid = "";
	public String signin_enter_valid_email = "";
	public String signin_device_added_success = "";
	public String signin_your_cashew_id = "";
	public String signin_login_fail = "";
	public String signin_your_loment_id = "";
	public String btn_sign_in = "";

	// <!-- Sign Up -->
	public String register_progress_message = "";
	public String spinner_country = "";
	public String btn_register = "";
	public String et_name = "";
	public String et_email1 = "";
	public String country_code = "";
	public String et_mobile = "";
	public String et_password_confirm = "";
	public String et_devicename = "";
	public String signup_username_min_2chars = "";
	public String signup_valid_email = "";
	public String signup_mobile_number_7char = "";
	public String signup_password_min_4char = "";
	public String error_passwords_are_not_equal = "";
	public String signup_lomentid_already_exists = "";
	public String signup_loment_details = "";
	public String dlg_reg = "";

	// <!-- Login -->
	public String remember_password = "";
	public String btn_login = "";
	public String login_enter_password = "";
	public String login_incorrect_password = "";
	public String login_please_wait = "";
	public String dlg_login = "";
	public String login_link_sent_to = "";
	public String login_failed_to_send = "";
	public String login_failed_to_send1 = "";
	public String login_failed_to_send2 = "";
	// <!-- Account -->
	public String et_cashewnut_id = "";
	public String et_select_cashewnut_id = "";
	public String btn_add_acc = "";
	public String account_create_cashew_id = "";
	public String msg_cashewnut_id_exist = "";
	public String dlg_add_cashewnut_id = "";
	public String valid_cashewnut_id = "";

	// <!-- About -->
	public String about_comment = "";
	public String about_version = "";
	public String about_build_time = "";
	public String about_email = "";

	// <!-- Activate Subscription -->
	public String activate_subscription = "";
	public String activate_subscribe = "";
	public String activate_no_activation_key = "";
	public String activate_click_here = "";
	public String activate_primary_loment_id = "";
	public String activate_activation_key = "";
	public String activate_cashewnut_successfully = "";
	public String activate_trial = "";
	public String activate_active = "";
	public String activate_account_not_found = "";
	public String activate_primary_username_activationkey_not_found = "";
	public String activate_email_support = "";
	public String activate_paid = "";

	// <!-- Compose -->
	public String compose_compose = "";
	public String message_compose_to_hint = "";
	public String message_compose_valid_recipient = "";
	public String message_compose_not_exist = "";
	public String message_compose_enter_message_tosend = "";
	public String compose_message_settings = "";
	public String compose_future_time_for_message = "";
	public String compose_play = "";
	public String message_compose_description_add_to = "";
	public String image = "";

	// <!-- Load Contacts -->
	public String contacts_choose_id = "";
	public String contacts_receipient = "";

	// <!-- Country List View -->
	public String country_search = "";

	// <!-- Settings -->
	public String settings_account = "";
	public String settings_change_password = "";
	public String settings_settings_privacy = "";
	public String settings_notification_settings = "";
	public String settings_play_sound = "";
	public String settings_additional_settings = "";
	public String settings_enable_login_screen = "";
	public String settings_hibernate = "";
	public String settings_language = "";
	public String settings_auto_response = "";
	public String settings_auto_response_message = "";
	public String settings_save = "";
	public String settings_highest = "";
	public String settings_high = "";
	public String settings_medium = "";
	public String settings_low = "";
	public String settings_saved = "";
	public String settings_notification = "";

	// <!-- Edit Password -->
	public String edit_old_password = "";
	public String edit_new_password = "";
	public String edit_confirm_password = "";
	public String edit_password_changed_successfully = "";
	public String edit_password_not_updated_on_server = "";
	public String edit_change_password = "";
	public String edit_password_lessthan_4chars = "";
	public String edit_new_repeat_passwords_not_same = "";
	public String edit_old_password_not_match = "";

	// <!-- File Picker -->
	public String file_picker_no_directories = "";
	public String file_picker_file_name = "";

	// <!-- Image Attachment -->
	public String image_attachment_view = "";
	public String image_attachment_remove = "";

	// <!-- Message Details -->
	public String message_details_from = "";
	public String message_details_to = "";
	public String message_details_expiry_time = "";
	public String message_details_schedule = "";
	public String message_details_time = "";
	public String message_restricted_time = "";

	// <!-- Profile Item -->
	public String profile_name = "";
	public String profile_app_name = "";
	public String member_type = "";
	public String profile_name1 = "";
	public String profile_app_name1 = "";
	public String profile_participants = "";

	// <!-- Reports -->
	public String reports_registration_details = "";
	public String reports_loment_id = "";
	public String reports_subscription_details = "";
	public String reports_type = "";
	public String reports_status = "";
	public String reports_start_date = "";
	public String reports_end_date = "";
	public String reports_account_subscription = "";
	public String reports_activate_subscription = "";

	// <!-- Search -->
	public String search_hint = "";
	public String search_search = "";

	// <!-- Conversation list & view -->
	public String member_added_successfully = "";
	public String member_deleted_successfully = "";
	public String admin_changed = "";
	public String edit_groupname = "";
	public String you_are_not_a_member_of_this_group = "";
	public String groupname_changed = "";
	public String group_created = "";
	public String are_added_to_group = "";
	public String added_to_group = "";
	public String moved_out_of_this_group = "";
	public String group_name_changed_to = "";
	public String group_admin_changed_to = "";;
	public String please_update_latest_release = "";
	public String delete_member = "";
	public String send_message = "";
	public String set_group_admin = "";
	public String group_name_hint = "";
	public String contextual_view = "";
	public String contextual_delete = "";
	public String contextual_copy = "";
	public String contextual_forward = "";
	public String contextual_refresh = "";
	public String conversation_newgroup = "";
	public String conversation_groups = "";
	public String conversation_contacts = "";
	public String conversation_request = "";
	public String conversation_friend_request = "";
	public String conversation_settings = "";
	public String conversation_signout = "";
	public String conversation_reports = "";
	public String conversation_about = "";
	public String ConversationFragment_confirm_message_delete = "";
	public String ConversationFragment_are_you_sure_you_want_to_permanently_delete_this_message = "";
	public String conversation_delete_group = "";
	public String conversation_delete_messages = "";
	public String conversation_groupinfo = "";
	public String conversation_contactinfo = "";
	public String conversation_exitfromgroup = "";
	public String conversation_view_recipient_id = "";
	public String conversation_view_group = "";
	public String conversation_recipients = "";
	public String conversation_you_cant = "";
	public String conversation_restricted_messages = "";
	public String conversation_ack_messages = "";
	public String conversation_message_details = "";
	public String conversation_acknowledgement = "";
	public String conversation_send_acknowledgement = "";
	public String conversation_view_downloading = "";
	public String conversation_view_selected = "";
	public String friend_allow = "";
	public String friend_ignore = "";

	// <!-- Conversation primer -->
	public String conversation_unknown_contact = "";
	public String contextual_resend = "";

	// <!-- Compose Menu -->
	public String compose_attach = "";
	public String attach_image = "";
	public String attach_take_photo = "";
	public String attach_video = "";
	public String attach_record_video = "";
	public String select_attach_video = "";
	public String attach_audio = "";
	public String attach_record_audio = "";
	public String select_attach_audio = "";
	public String attach_document = "";
	public String recording_started = "";
	// <!-- Cashewnut Application -->
	public String app_message_from = "";
	public String app_messages_from = "";
	public String app_receive_message = "";
	public String app_message_conversation = "";

	// <!-- Message Sender -->
	public String sender_network_unavailable = "";
	public String no_interner_connection = "";
	// <!-- Message Control Parameters -->
	public String message_acknowledgement = "";
	// <!-- Manifest file -->
	public String sign_in1 = "";
	public String accounts1 = "";
	public String login = "";
	public String sign_up1 = "";
	public String search_country = "";
	public String convetsation_list = "";
	public String compose = "";
	public String contacts = "";
	public String selectcontacts = "";
	public String creategroup = "";
	public String newgroup = "";
	public String next = "";
	public String contacts_info = "";
	public String group_info = "";
	public String settings = "";
	public String change_password = "";
	public String reports = "";
	public String about_us = "";
	public String selectfile = "";
	public String activate_subscription1 = "";
	public String compose_activate_subscription = "";

	public String english = "";
	public String chinese = "";
	public String french = "";
	public String message_zoom_touch_expand = "";
	public String description_zoom_touch_close = "";
	// <!-- Intro view -->
	public String Page1Title = "";
	public String Page2Title = "";
	public String Page3Title = "";
	public String Page1Message = "";
	public String Page2Message = "";
	public String Page3Message = "";
	public String slide_to_cancel = "";

	// <!-- New Strings -->
	public String group_admin = "";
	public String please_enter = "";
	public String primary_payer_username = "";
	public String activation_Key = "";
	public String and = "";
	public String load_more = "";
	public String valid_password = "";
	public String enter_valid_password = "";
	public String subscription_inactive = "";
	public String subscription_enter_activationkey = "";
	public String find_friend = "";
	public String friend_allow1 = "";
	public String friend_ignore1 = "";
	public String search_contact_hint = "";
	public String search_contact_error = "";
	public String search_contact = "";
	public String searchin_on_server = "";
	public String app_request_from = "";
	public String app_receive_request = "";
	public String search_completed = "";
	public String add_friend = "";
	public String req_sent = "";
	public String contact_comment = "";
	public String change_group_name_hint = "";
	public String create_new_id = "";
	public String messenger_id_is = "";
	public String messenger_id_is1 = "";
	public String messenger_id_usage1 = "";
	public String et_select_cashewnut_id1 = "";
	public String create_account = "";
	public String messenger_id_usage = "";
	public String accounts2 = "";
	public String conversation_reports1 = "";
	public String add_contact = "";
	public String added_contact = "";
	public String smstext = "";
	public String smstext1 = "";
	public String emailsubject = "";
	public String invitefriends = "";
	public String invite = "";
	public String invite_phone = "";
	public String invite_email = "";
	public String settings_vibrate = "";
	public String paymentlink = "";
	public String support_email = "";
	public String website = "";
	

	// <!-- 2 New Strings -->
	public String select_language = "";
	public String signup_comment = "";
	public String suggestions = "";
	public String requests = "";
	public String rejected = "";

	// new strings
	public String message_compose_to_error = "";
	public String select_one_participent = "";
	public String group_name_error_hint = "";
	public String group_name = "";
	public String error_fields_required = "";
	public String cashew_id = "";
	public String select_attachment = "";
	public String font = "";
	public String conversation_not_member = "";
	public String conversation_date = "";
	public String conversation_chat_list = "";
	public String conversation_group_list = "";
	public String conversation_label = "";
	public String conversation_click_to_view = "";
	public String conversation_password_incorrect = "";
	public String conversation_password_incorrect1 = "";
	public String group_admin1 = "";
	public String login_enter_valid_password = "";
	public String valid_cashewnut_id1 = "";
	public String support_loment_net = "";
	public String website_loment_net = "";
	public String safecom_registration_link = "";
	public String phone_number_check = "";
	public String signup_fail = "";
	public String create_an_account = "";
	public String btn_sign1 = "";
	public String btn_signup1 = "";
	public String accounts = "";
	public String cashew_comments = "";
	public String website_loment_name = "";
	public String lomentid_hint_string = "";
	public String cashewid_hint_string = "";
	public String confirm_email = "";
	public String message_expiry = "";
	// new group Strings
	public String you = "";
	public String new_Group_Created = "";
	public String new_Member_Added = "";
	public String member_Exit = "";
	public String member_Removed = "";
	public String change_Group_Name = "";
	public String are_now_an_mainAdmin = "";
	public String is_now_an_mainAdmin = "";
	public String made = "";
	public String an_Admin = "";
	public String changed = "";
	public String to_a_member = "";
	public String feature_added = "";
	public String feature_removed = "";
	public String feature4_enable = "";
	public String feature4_disable = "";
	public String confirm = "";
	public String are_you_sure_you_want_to_change = "";
	public String change_member = "";
	public String Main_Admin = "";
	public String make_admin = ""; 
	public String Make_Main_Admin = "";
	public String reports_userName = "";
	public String reports_number = "";
	public String select_country = "";
	public String connection_lost = "";
	public String contact_search = "";
	public String forwardAttachment = "";
	public String forgotPasswordLinkSent="";
	public String contactRequests="";
	public void setStringsAqua() {
		try {
			if (i18NResources == null) {
				return;
			}
			app_name = i18NResources.getValue("app_name");
			app_name_bold = i18NResources.getValue("app_name_bold");
			app_name_bold = i18NResources.getValue("app_name_bold");
			et_password = i18NResources.getValue("et_password");
			login_progress_signing_in = i18NResources
					.getValue("login_progress_signing_in");
			forgot_password = i18NResources.getValue("forgot_password");
			compose_type_message = i18NResources
					.getValue("compose_type_message");
			dlg_ok = i18NResources.getValue("dlg_ok");
			dlg_cancel = i18NResources.getValue("dlg_cancel");
			dialog_priority_indicator = i18NResources
					.getValue("dialog_priority_indicator");
			dialog_expiry_in = i18NResources.getValue("dialog_expiry_in");
			forward_backup_restricted = i18NResources
					.getValue("forward_backup_restricted");
			dialog_read_ack = i18NResources.getValue("dialog_read_ack");
			dialog_schedule_message = i18NResources
					.getValue("dialog_schedule_message");
			dialog_mins = i18NResources.getValue("dialog_mins");
			dialog_done = i18NResources.getValue("dialog_done");
			contacts_new = i18NResources.getValue("contacts_new");

			error_field_required = i18NResources
					.getValue("error_field_required");
			yes = i18NResources.getValue("yes");
			no = i18NResources.getValue("no");
			ok = i18NResources.getValue("ok");
			;
			attachment_size_exceeds = i18NResources
					.getValue("attachment_size_exceeds");
			download_attachment_first = i18NResources
					.getValue("download_attachment_first");
			add_member = i18NResources.getValue("add_member");
			// <!-- Splash -->
			splash_loading = i18NResources.getValue("splash_loading");

			// <!-- Sign In -->
			btn_sign = i18NResources.getValue("btn_sign");
			need_an_account = i18NResources.getValue("need_an_account");
			et_username1 = i18NResources.getValue("et_username1");
			signin_enter_lomentid = i18NResources
					.getValue("signin_enter_lomentid");
			signin_enter_valid_email = i18NResources
					.getValue("signin_enter_valid_email");
			signin_device_added_success = i18NResources
					.getValue("signin_device_added_success");
			signin_your_cashew_id = i18NResources
					.getValue("signin_your_cashew_id");
			signin_login_fail = i18NResources.getValue("signin_login_fail");
			signin_your_loment_id = i18NResources
					.getValue("signin_your_loment_id");
			btn_sign_in = i18NResources.getValue("btn_sign_in");
			btn_signup = i18NResources.getValue("btn_signup");

			// <!-- Sign Up -->
			register_progress_message = i18NResources
					.getValue("register_progress_message");
			spinner_country = i18NResources.getValue("spinner_country");
			btn_register = i18NResources.getValue("btn_register");
			et_name = i18NResources.getValue("et_name");
			et_email1 = i18NResources.getValue("et_email1");
			country_code = i18NResources.getValue("country_code");
			et_mobile = i18NResources.getValue("et_mobile");
			et_password_confirm = i18NResources.getValue("et_password_confirm");
			et_devicename = i18NResources.getValue("et_devicename");
			signup_username_min_2chars = i18NResources
					.getValue("signup_username_min_2chars");
			signup_valid_email = i18NResources.getValue("signup_valid_email");
			signup_mobile_number_7char = i18NResources
					.getValue("signup_mobile_number_7char");
			signup_password_min_4char = i18NResources
					.getValue("signup_password_min_4char");
			error_passwords_are_not_equal = i18NResources
					.getValue("error_passwords_are_not_equal");
			signup_lomentid_already_exists = i18NResources
					.getValue("signup_lomentid_already_exists");
			signup_loment_details = i18NResources
					.getValue("signup_loment_details");
			dlg_reg = i18NResources.getValue("dlg_reg");

			// <!-- Login -->
			remember_password = i18NResources.getValue("remember_password");
			btn_login = i18NResources.getValue("btn_login");
			login_enter_password = i18NResources
					.getValue("login_enter_password");
			login_incorrect_password = i18NResources
					.getValue("login_incorrect_password");
			login_please_wait = i18NResources.getValue("login_please_wait");
			dlg_login = i18NResources.getValue("dlg_login");
			login_link_sent_to = i18NResources.getValue("login_link_sent_to");
			login_failed_to_send = i18NResources
					.getValue("login_failed_to_send");
			login_failed_to_send1 = i18NResources
					.getValue("login_failed_to_send1");
			login_failed_to_send2 = i18NResources
					.getValue("login_failed_to_send2");
			// <!-- Account -->
			et_cashewnut_id = i18NResources.getValue("et_cashewnut_id");
			et_select_cashewnut_id = i18NResources
					.getValue("et_select_cashewnut_id");
			btn_add_acc = i18NResources.getValue("btn_add_acc");
			account_create_cashew_id = i18NResources
					.getValue("account_create_cashew_id");
			msg_cashewnut_id_exist = i18NResources
					.getValue("msg_cashewnut_id_exist");
			dlg_add_cashewnut_id = i18NResources
					.getValue("dlg_add_cashewnut_id");
			valid_cashewnut_id = i18NResources.getValue("valid_cashewnut_id");
			// <!-- About -->
			about_comment = i18NResources.getValue("about_comment");
			about_version = i18NResources.getValue("about_version");
			about_build_time = i18NResources.getValue("about_build_time");
			about_email = i18NResources.getValue("about_email");

			// <!-- Activate Subscription -->
			activate_subscription = i18NResources
					.getValue("activate_subscription");
			activate_subscribe = i18NResources.getValue("activate_subscribe");
			activate_no_activation_key = i18NResources
					.getValue("activate_no_activation_key");
			activate_click_here = i18NResources.getValue("activate_click_here");
			activate_primary_loment_id = i18NResources
					.getValue("activate_primary_loment_id");
			activate_activation_key = i18NResources
					.getValue("activate_activation_key");
			activate_cashewnut_successfully = i18NResources
					.getValue("activate_cashewnut_successfully");
			activate_trial = i18NResources.getValue("activate_trial");
			activate_active = i18NResources.getValue("activate_active");
			activate_account_not_found = i18NResources
					.getValue("activate_account_not_found");
			activate_primary_username_activationkey_not_found = i18NResources
					.getValue("activate_primary_username_activationkey_not_found");
			activate_email_support = i18NResources
					.getValue("activate_email_support");
			activate_paid = i18NResources.getValue("activate_paid");

			// <!-- Compose -->
			compose_compose = i18NResources.getValue("compose_compose");
			message_compose_to_hint = i18NResources
					.getValue("message_compose_to_hint");
			message_compose_valid_recipient = i18NResources
					.getValue("message_compose_valid_recipient");
			message_compose_not_exist = i18NResources
					.getValue("message_compose_not_exist");
			message_compose_enter_message_tosend = i18NResources
					.getValue("message_compose_enter_message_tosend");
			compose_message_settings = i18NResources
					.getValue("compose_message_settings");
			compose_future_time_for_message = i18NResources
					.getValue("compose_future_time_for_message");
			compose_play = i18NResources.getValue("compose_play");
			message_compose_description_add_to = i18NResources
					.getValue("message_compose_description_add_to");
			image = i18NResources.getValue("image");
			// <!-- Load Contacts -->
			contacts_choose_id = i18NResources.getValue("contacts_choose_id");
			contacts_receipient = i18NResources.getValue("contacts_receipient");

			// <!-- Country List View -->
			country_search = i18NResources.getValue("country_search");

			// <!-- Settings -->
			settings_account = i18NResources.getValue("settings_account");
			settings_change_password = i18NResources
					.getValue("settings_change_password");
			settings_settings_privacy = i18NResources
					.getValue("settings_settings_privacy");
			settings_notification_settings = i18NResources
					.getValue("settings_notification_settings");
			settings_play_sound = i18NResources.getValue("settings_play_sound");
			settings_additional_settings = i18NResources
					.getValue("settings_additional_settings");
			settings_enable_login_screen = i18NResources
					.getValue("settings_enable_login_screen");
			settings_hibernate = i18NResources.getValue("settings_hibernate");
			settings_language = i18NResources.getValue("settings_language");
			settings_auto_response = i18NResources
					.getValue("settings_auto_response");
			settings_auto_response_message = i18NResources
					.getValue("settings_auto_response_message");
			settings_save = i18NResources.getValue("settings_save");
			settings_highest = i18NResources.getValue("settings_highest");
			settings_high = i18NResources.getValue("settings_high");
			settings_medium = i18NResources.getValue("settings_medium");
			settings_low = i18NResources.getValue("settings_low");
			settings_saved = i18NResources.getValue("settings_saved");
			settings_notification = i18NResources
					.getValue("settings_notification");

			// <!-- Edit Password -->
			edit_old_password = i18NResources.getValue("edit_old_password");
			edit_new_password = i18NResources.getValue("edit_new_password");
			edit_confirm_password = i18NResources
					.getValue("edit_confirm_password");
			edit_password_changed_successfully = i18NResources
					.getValue("edit_password_changed_successfully");
			edit_password_not_updated_on_server = i18NResources
					.getValue("edit_password_not_updated_on_server");
			edit_change_password = i18NResources
					.getValue("edit_change_password");
			edit_password_lessthan_4chars = i18NResources
					.getValue("edit_password_lessthan_4chars");
			edit_new_repeat_passwords_not_same = i18NResources
					.getValue("edit_new_repeat_passwords_not_same");
			edit_old_password_not_match = i18NResources
					.getValue("edit_old_password_not_match");

			// <!-- File Picker -->
			file_picker_no_directories = i18NResources
					.getValue("file_picker_no_directories");
			;
			file_picker_file_name = i18NResources
					.getValue("file_picker_file_name");

			// <!-- Image Attachment -->
			image_attachment_view = i18NResources
					.getValue("image_attachment_view");
			image_attachment_remove = i18NResources
					.getValue("image_attachment_remove");

			// <!-- Message Details -->
			message_details_from = i18NResources
					.getValue("message_details_from");
			message_details_to = i18NResources.getValue("message_details_to");
			message_details_expiry_time = i18NResources
					.getValue("message_details_expiry_time");
			message_details_schedule = i18NResources
					.getValue("message_details_schedule");
			message_details_time = i18NResources
					.getValue("message_details_time");
			message_restricted_time = i18NResources
					.getValue("message_restricted_time");

			// <!-- Profile Item -->
			profile_name = i18NResources.getValue("profile_name");
			profile_app_name = i18NResources.getValue("profile_app_name");
			member_type = i18NResources.getValue("member_type");
			profile_name1 = i18NResources.getValue("profile_name1");
			profile_app_name1 = i18NResources.getValue("profile_app_name1");
			profile_participants = i18NResources
					.getValue("profile_participants");

			// <!-- Reports -->
			reports_registration_details = i18NResources
					.getValue("reports_registration_details");
			reports_loment_id = i18NResources.getValue("reports_loment_id");
			reports_subscription_details = i18NResources
					.getValue("reports_subscription_details");
			reports_type = i18NResources.getValue("reports_type");
			reports_status = i18NResources.getValue("reports_status");
			reports_start_date = i18NResources.getValue("reports_start_date");
			reports_end_date = i18NResources.getValue("reports_end_date");
			reports_account_subscription = i18NResources
					.getValue("reports_account_subscription");
			reports_activate_subscription = i18NResources
					.getValue("reports_activate_subscription");

			// <!-- Search -->
			search_hint = i18NResources.getValue("search_hint");
			search_search = i18NResources.getValue("search_search");

			// <!-- Conversation list & view -->
			member_added_successfully = i18NResources
					.getValue("member_added_successfully");
			member_deleted_successfully = i18NResources
					.getValue("member_deleted_successfully");
			admin_changed = i18NResources.getValue("admin_changed");
			edit_groupname = i18NResources.getValue("edit_groupname");
			you_are_not_a_member_of_this_group = i18NResources
					.getValue("you_are_not_a_member_of_this_group");
			groupname_changed = i18NResources.getValue("groupname_changed");
			group_created = i18NResources.getValue("group_created");
			are_added_to_group = i18NResources.getValue("are_added_to_group");
			added_to_group = i18NResources.getValue("added_to_group");
			moved_out_of_this_group = i18NResources
					.getValue("moved_out_of_this_group");
			group_name_changed_to = i18NResources
					.getValue("group_name_changed_to");
			group_admin_changed_to = i18NResources
					.getValue("group_admin_changed_to");
			please_update_latest_release = i18NResources
					.getValue("please_update_latest_release");
			delete_member = i18NResources.getValue("delete_member");
			send_message = i18NResources.getValue("send_message");
			set_group_admin = i18NResources.getValue("set_group_admin");
			group_name_hint = i18NResources.getValue("group_name_hint");
			contextual_view = i18NResources.getValue("contextual_view");
			contextual_delete = i18NResources.getValue("contextual_delete");
			contextual_copy = i18NResources.getValue("contextual_copy");
			contextual_forward = i18NResources.getValue("contextual_forward");
			contextual_refresh = i18NResources.getValue("contextual_refresh");
			conversation_newgroup = i18NResources
					.getValue("conversation_newgroup");
			conversation_groups = i18NResources.getValue("conversation_groups");
			conversation_contacts = i18NResources
					.getValue("conversation_contacts");
			conversation_request = i18NResources
					.getValue("conversation_request");
			conversation_friend_request = i18NResources
					.getValue("conversation_friend_request");
			conversation_settings = i18NResources
					.getValue("conversation_settings");
			conversation_signout = i18NResources
					.getValue("conversation_signout");
			conversation_reports = i18NResources
					.getValue("conversation_reports");
			conversation_about = i18NResources.getValue("conversation_about");
			ConversationFragment_confirm_message_delete = i18NResources
					.getValue("ConversationFragment_confirm_message_delete");
			ConversationFragment_are_you_sure_you_want_to_permanently_delete_this_message = i18NResources
					.getValue("ConversationFragment_are_you_sure_you_want_to_permanently_delete_this_message");
			conversation_delete_group = i18NResources
					.getValue("conversation_delete_group");
			conversation_delete_messages = i18NResources
					.getValue("conversation_delete_messages");
			conversation_groupinfo = i18NResources
					.getValue("conversation_groupinfo");
			conversation_contactinfo = i18NResources
					.getValue("conversation_contactinfo");
			conversation_exitfromgroup = i18NResources
					.getValue("conversation_exitfromgroup");
			conversation_view_recipient_id = i18NResources
					.getValue("conversation_view_recipient_id");
			conversation_view_group = i18NResources
					.getValue("conversation_view_group");
			conversation_recipients = i18NResources
					.getValue("conversation_recipients");
			conversation_you_cant = i18NResources
					.getValue("conversation_you_cant");
			conversation_restricted_messages = i18NResources
					.getValue("conversation_restricted_messages");
			conversation_ack_messages = i18NResources
					.getValue("conversation_ack_messages");
			conversation_message_details = i18NResources
					.getValue("conversation_message_details");
			conversation_acknowledgement = i18NResources
					.getValue("conversation_acknowledgement");
			conversation_send_acknowledgement = i18NResources
					.getValue("conversation_send_acknowledgement");
			conversation_view_downloading = i18NResources
					.getValue("conversation_view_downloading");
			conversation_view_selected = i18NResources
					.getValue("conversation_view_selected");
			friend_allow = i18NResources.getValue("friend_allow");
			friend_ignore = i18NResources.getValue("friend_ignore");

			// <!-- Conversation primer -->
			conversation_unknown_contact = i18NResources
					.getValue("conversation_unknown_contact");
			contextual_resend = i18NResources.getValue("contextual_resend");

			// <!-- Compose Menu -->
			compose_attach = i18NResources.getValue("compose_attach");
			attach_image = i18NResources.getValue("attach_image");
			attach_take_photo = i18NResources.getValue("attach_take_photo");
			attach_video = i18NResources.getValue("attach_video");
			attach_record_video = i18NResources.getValue("attach_record_video");
			select_attach_video = i18NResources.getValue("select_attach_video");
			attach_audio = i18NResources.getValue("attach_audio");
			attach_record_audio = i18NResources.getValue("attach_record_audio");
			select_attach_audio = i18NResources.getValue("select_attach_audio");
			attach_document = i18NResources.getValue("attach_document");
			recording_started = i18NResources.getValue("recording_started");
			// <!-- Cashewnut Application -->
			app_message_from = i18NResources.getValue("app_message_from");
			app_messages_from = i18NResources.getValue("app_messages_from");
			app_receive_message = i18NResources.getValue("app_receive_message");
			app_message_conversation = i18NResources
					.getValue("app_message_conversation");

			// <!-- Message Sender -->
			sender_network_unavailable = i18NResources
					.getValue("sender_network_unavailable");
			no_interner_connection = i18NResources
					.getValue("no_interner_connection");

			// <!-- Message Control Parameters -->
			message_acknowledgement = i18NResources
					.getValue("message_acknowledgement");
			// <!-- Manifest file -->
			sign_in1 = i18NResources.getValue("sign_in1");
			accounts1 = i18NResources.getValue("accounts1");
			login = i18NResources.getValue("login");
			sign_up1 = i18NResources.getValue("sign_up1");
			search_country = i18NResources.getValue("search_country");
			convetsation_list = i18NResources.getValue("convetsation_list");
			compose = i18NResources.getValue("compose");
			contacts = i18NResources.getValue("contacts");
			selectcontacts = i18NResources.getValue("selectcontacts");
			creategroup = i18NResources.getValue("creategroup");
			newgroup = i18NResources.getValue("newgroup");
			next = i18NResources.getValue("next");
			contacts_info = i18NResources.getValue("contacts_info");
			group_info = i18NResources.getValue("group_info");
			settings = i18NResources.getValue("settings");
			change_password = i18NResources.getValue("change_password");
			reports = i18NResources.getValue("reports");
			about_us = i18NResources.getValue("about_us");
			selectfile = i18NResources.getValue("selectfile");
			activate_subscription1 = i18NResources
					.getValue("activate_subscription1");
			;
			compose_activate_subscription = i18NResources
					.getValue("compose_activate_subscription");

			english = i18NResources.getValue("english");
			chinese = i18NResources.getValue("chinese");
			french = i18NResources.getValue("french");
			message_zoom_touch_expand = i18NResources
					.getValue("message_zoom_touch_expand");
			description_zoom_touch_close = i18NResources
					.getValue("description_zoom_touch_close");

			// <!-- Intro view -->
			Page1Title = i18NResources.getValue("Page1Title");
			Page2Title = i18NResources.getValue("Page2Title");
			Page3Title = i18NResources.getValue("Page3Title");
			Page1Message = i18NResources.getValue("Page1Message");
			Page2Message = i18NResources.getValue("Page2Message");
			Page3Message = i18NResources.getValue("Page3Message");
			slide_to_cancel = i18NResources.getValue("slide_to_cancel");

			// <!-- New Strings -->
			group_admin = i18NResources.getValue("group_admin");
			please_enter = i18NResources.getValue("please_enter");
			primary_payer_username = i18NResources
					.getValue("primary_payer_username");
			activation_Key = i18NResources.getValue("activation_Key");
			and = i18NResources.getValue("and");
			load_more = i18NResources.getValue("load_more");
			valid_password = i18NResources.getValue("valid_password");
			enter_valid_password = i18NResources
					.getValue("enter_valid_password");
			subscription_inactive = i18NResources
					.getValue("subscription_inactive");
			subscription_enter_activationkey = i18NResources
					.getValue("subscription_enter_activationkey");
			find_friend = i18NResources.getValue("find_friend");
			friend_allow1 = i18NResources.getValue("friend_allow1");
			friend_ignore1 = i18NResources.getValue("friend_ignore1");
			search_contact_hint = i18NResources.getValue("search_contact_hint");
			search_contact_error = i18NResources
					.getValue("search_contact_error");
			search_contact = i18NResources.getValue("search_contact");
			searchin_on_server = i18NResources.getValue("searchin_on_server");
			app_request_from = i18NResources.getValue("app_request_from");
			app_receive_request = i18NResources.getValue("app_receive_request");
			search_completed = i18NResources.getValue("search_completed");
			add_friend = i18NResources.getValue("add_friend");
			req_sent = i18NResources.getValue("req_sent");
			contact_comment = i18NResources.getValue("contact_comment");
			change_group_name_hint = i18NResources
					.getValue("change_group_name_hint");
			create_new_id = i18NResources.getValue("create_new_id");
			messenger_id_is = i18NResources.getValue("messenger_id_is");
			messenger_id_is1 = i18NResources.getValue("messenger_id_is1");
			messenger_id_usage1 = i18NResources.getValue("messenger_id_usage1");
			et_select_cashewnut_id1 = i18NResources
					.getValue("et_select_cashewnut_id1");
			create_account = i18NResources.getValue("create_account");
			messenger_id_usage = i18NResources.getValue("messenger_id_usage");
			accounts2 = i18NResources.getValue("accounts2");
			conversation_reports1 = i18NResources
					.getValue("conversation_reports1");
			add_contact = i18NResources.getValue("add_contact");
			added_contact = i18NResources.getValue("added_contact");
			smstext = i18NResources.getValue("smstext");
			smstext1 = i18NResources.getValue("smstext1");
			emailsubject = i18NResources.getValue("emailsubject");
			invitefriends = i18NResources.getValue("invitefriends");
			invite = i18NResources.getValue("invite");
			invite_phone = i18NResources.getValue("invite_phone");
			invite_email = i18NResources.getValue("invite_email");
			settings_vibrate = i18NResources.getValue("settings_vibrate");
			paymentlink = i18NResources.getValue("paymentlink");
			support_email = i18NResources.getValue("support_email");
			website = i18NResources.getValue("website");

			// <!-- 2 New Strings -->
			select_language = i18NResources.getValue("select_language");
			signup_comment = i18NResources.getValue("signup_comment");
			suggestions = i18NResources.getValue("suggestions");
			requests = i18NResources.getValue("requests");
			rejected = i18NResources.getValue("rejected");

			// new strings

			message_compose_to_error = i18NResources
					.getValue("message_compose_to_error");
			select_one_participent = i18NResources
					.getValue("select_one_participent");
			group_name_error_hint = i18NResources
					.getValue("group_name_error_hint");
			group_name = i18NResources.getValue("group_name");
			error_fields_required = i18NResources
					.getValue("error_fields_required");
			cashew_id = i18NResources.getValue("cashew_id");
			select_attachment = i18NResources.getValue("select_attachment");
			font = i18NResources.getValue("font");
			conversation_not_member = i18NResources
					.getValue("conversation_not_member");
			conversation_date = i18NResources.getValue("conversation_date");
			conversation_chat_list = i18NResources
					.getValue("conversation_chat_list");
			conversation_group_list = i18NResources
					.getValue("conversation_group_list");
			conversation_label = i18NResources.getValue("conversation_label");
			conversation_click_to_view = i18NResources
					.getValue("conversation_click_to_view");
			conversation_password_incorrect = i18NResources
					.getValue("conversation_password_incorrect");
			conversation_password_incorrect1 = i18NResources
					.getValue("conversation_password_incorrect1");
			group_admin1 = i18NResources.getValue("group_admin1");
			login_enter_valid_password = i18NResources
					.getValue("login_enter_valid_password");

			valid_cashewnut_id1 = i18NResources.getValue("valid_cashewnut_id1");

			support_loment_net = i18NResources.getValue("support_loment_net");

			website_loment_net = i18NResources.getValue("website_loment_net");
			safecom_registration_link = i18NResources
					.getValue("safecom_registration_link");
			phone_number_check = i18NResources.getValue("phone_number_check");
			signup_fail = i18NResources.getValue("signup_fail");
			create_an_account = i18NResources.getValue("create_an_account");
			btn_sign1 = i18NResources.getValue("btn_sign1");
			btn_signup1 = i18NResources.getValue("btn_signup1");

			accounts = i18NResources.getValue("accounts");
			cashew_comments = i18NResources.getValue("cashew_comments");
			website_loment_name = i18NResources.getValue("website_loment_name");
			lomentid_hint_string = i18NResources.getValue("lomentid_hint_string");
			cashewid_hint_string = i18NResources.getValue("cashewid_hint_string");
			confirm_email = i18NResources.getValue("confirm_email");
			
			// new group strings
			
			you = i18NResources.getValue("you");
			new_Group_Created = i18NResources.getValue("new_Group_Created");
			new_Member_Added = i18NResources.getValue("new_Member_Added");
			member_Exit = i18NResources.getValue("member_Exit");
			member_Removed = i18NResources.getValue("member_Removed");
			change_Group_Name = i18NResources.getValue("change_Group_Name");
			are_now_an_mainAdmin = i18NResources.getValue("are_now_an_mainAdmin");
			is_now_an_mainAdmin = i18NResources.getValue("is_now_an_mainAdmin");
			made = i18NResources.getValue("made");
			an_Admin = i18NResources.getValue("an_Admin");
			changed = i18NResources.getValue("changed");
			to_a_member = i18NResources.getValue("to_a_member");
			feature_added = i18NResources.getValue("feature_added");
			feature_removed = i18NResources.getValue("feature_removed");
			feature4_enable = i18NResources.getValue("feature4_enable");
			feature4_disable = i18NResources.getValue("feature4_disable");
			confirm = i18NResources.getValue("confirm");
			are_you_sure_you_want_to_change = i18NResources.getValue("are_you_sure_you_want_to_change");
			change_member = i18NResources.getValue("change_member");
			Main_Admin = i18NResources.getValue("Main_Admin");
			Make_Main_Admin = i18NResources.getValue("Make_Main_Admin");
			make_admin = i18NResources.getValue("make_admin");
			//new strings in profile edit
			reports_userName = i18NResources.getValue("reports_userName");
			reports_number = i18NResources.getValue("reports_number");
			select_country = i18NResources.getValue("select_country");
			connection_lost = i18NResources.getValue("connection_lost");
			message_expiry=i18NResources.getValue("ExpiryMessage");
			contact_search = i18NResources
					.getValue("contact_search");
			forwardAttachment = i18NResources
					.getValue("forwardAttachment");
			forgotPasswordLinkSent=i18NResources
					.getValue("forgotPasswordLinkSent");
			contactRequests=i18NResources
					.getValue("ContactRequests");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
