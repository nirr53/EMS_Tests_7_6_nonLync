package EMS_Tests;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
	
	Test1__login_1.class											,
	Test2__invalid_login.class										,
	Test3__home_screen_buttons.class								,
	Test4__devices_buttons.class									,
	Test5__add_user_tests.class										,
	Test6__add_device.class											,
	Test7__add_multiple_devices_to_same_user.class					,
	Test8__delete_devices_tests.class								,
	Test9__alarms_page_tests.class									,
	Test10__system_logs_tests.class									,
	Test11__import_configuration_tests.class						,
	Test12__import_users_devices_tests.class						,
	Test13__export_tests.class										,
	Test14__search_tests.class										,
	Test15__multiple_users_changes_reset_change_password.class		,
	Test16__multiple_users_changes_change_tenant.class				,
	Test17__multiple_users_update_change_configuration.class		,
	Test18__multiple_users_send_message_reset_device.class			,
	Test19__multiple_devices_change_language.class					,
	Test20__multiple_devices_send_message_delete_device.class		,
	Test21__multiple_devices_change_phone_type_reset_device.class	,
	Test22__multiple_devices_update_generate_configuration.class	,
	Test23__multiple_devices_change_firmware_vlan_mode.class		,
	Test24__templates_create_420_430_440_450_templates.class		,
	Test25__templates_create_450_405_templates.class				,
	Test26__tenant_placeholders.class								,
	Test27__system_settings.class									,
	Test28__template_placeholders.class								,
	Test29__tenant_configuration.class								,
	Test30__phone_configuration_files.class							,
	Test31__phone_firmware_files.class								,
	Test32__device_placeholders.class								,
	Test33__import_stop_and_not_continue.class						,
	Test34__import_stop_continue.class								,
	Test35__upload_conf_files_with_invalid_suffix.class				,
	Test36__add_bad_template.class									,
	Test37__Monitoring_Operation_Login_tests.class					,
	Test38__Monitoring_tenant_permissions.class						,
	Test39__Monitoring_Operation_Phone_configuration_menu.class		,
	Test40__Monitoring_user_tests.class								,
	Test41__Monitoring_phone_firmware_files.class					,
	Test42__Monitoring_System_settings.class						,
	Test43__Monitoring_Operation_Logs.class							,
	Test44__Operation_tenant_permissions.class						,
	Test45__Monitoring_system_tenant_placeholders.class				,
	Test46__Monitoring_template_placeholders.class					,
	Test47__Monitoring_device_placeholders.class					,
	Test48__Monitoring_system_multi_users_actions.class				,
	Test49__Monitoring_system_multi_devices_actions.class			,
	Test50__Monitoring_import.class									,
	Test51__Monitoring_tenant_multi_users_actions.class				,	
	Test52__Operation_System_Settings.class							,
	Test53__Operation_tenant_multi_users_actions.class				,
	Test54__Operation_import.class									,
	Test55__Operation_tenant_placeholders.class						,
	Test56__Operation_phone_firmware_files.class					,
	Test57__Operation_default_phone_firmware_files.class			,
	Test58__Operation_template_placeholders.class					,
	Test59__Operation_system_tenant_templates.class					,
	Test60__Operation_system_multi_users_actions.class				,
	Test61__Operation_user_actions.class							,
	Test62__Monitoring_export_tests.class							,
	Test63__Operation_export_tests.class							,
	Test64__long_name_users.class									,
	Test65__import_diffrent_langs_users.class						,
	Test66__SBC_Proxy_Option_configuration.class					,
	Test67__DHCP_Option_configuration.class							,
	Test68__DHCP_Option_configuration_urls.class					,
	Test69__Operation_multi_users_devices_permissions.class			,	
	Test70__search_tests.class										,
	Test71__Operation_system_multi_devices_actions.class			,
	Test72__Operation_tenant_multi_devices_actions.class			,
	Test73__templates_sherfiles_link.class							,
	Test74__multiple_users_export_results.class						,
	Test75__system_licsense.class									,
	Test76__long_location_users.class								,
	Test77__full_search.class										,
	Test78__different_users_same_mac.class							,
	Test79__edit_zero_tuch_user.class								,
	Test80__view_tenants.class										,
	Test81__view_sites.class										,
	Test82__site_configuration.class								,
	Test83__Monitoring_alarms.class									,
	Test84__Operation_alarms.class									,
	Test85__Monitoring_Opeartion_license.class						,
	Test86__Monitoring_operation_view_sites.class					,
	Test87__site_placeholders.class									,
	Test88__Monitoring_system_site_configuration.class				,
	Test89__Monitoring_tenant_site_configuration.class				,
	Test90__Monitoring_system_tenant_templates.class				,
	Test91__Monitoring_tenant_multi_devices_actions.class			,
	Test92__LDAP.class												,
	Test93__Monitoring_LDAP.class									,
	Test94__tenant_configuration_features.class						,
	Test95__site_configuration_features.class						,
	Test96__template_features.class									,
	Test97__device_actions.class									,
	Test98__device_actions_row.class								,
	Test99__Monitoring_device_actions.class							,
	Test100__Monitoring_device_actions_row.class					,
	Test101__device_status_export_tests.class						,
	Test102__device_status_filter_columns.class						,
	Test103__speicel_characters_users.class							,
	Test105__device_status_filter_tests.class						,
	Test106__device_status_nickname_tests.class						,
	Test107__system_settings_http_https.class						,
	Test108__alarms_tests.class										,
	Test109__alarms_tests2.class									,
	Test110__alarms_tests3.class									,
	Test111__multiple_browsers.class								,
	Test112__multiple_users_add_delete_configuration.class			,
	Test113__multiple_users_configuration_key_features.class		,
	Test114__different_BToE_status.class							,
	Test115__different_BToE_version_numbers.class					,
	Test116__upper_menu_buttons.class								,
	Test117__user_configuration.class								,
	Test118__user_configuration_features.class						,
	Test119__alarms_tests4.class									,
	Test120__private_placeholders.class								,
	Test121__network_topology.class									,
	Test122__registered_unregisterd_users_number.class				,
	Test123__change_status.class									,
	Test124__multiple_devices_actions_timing.class					,
	Test125__multiple_users_actions_timing.class					,
	Test126__timeout.class											,
	Test127__templates_with_speicel_characters.class				,
	Test128__template_exisiting_configuration_and_ph.class			,
	Test129__export_private_template_placeholder.class				,
	Test130__tenant_site_speicel_characters.class					,
	Test131__delete_device_tests.class								,
	Test132__timeout.class

})

public class TestSuite {

}
