import Vue from 'vue';
import Vuex from 'vuex';
//Login
import global from 'modules/global/store';
import login from 'modules/login/store';
import register from 'modules/register/store';
import users from 'modules/users/store';
import forgotpassword from 'modules/forgotpassword/store';
import resetstatus from 'modules/reset-status/store';
//User account management
import accountmanagement from 'modules/accountmanagement/store';
import accountmanagement_group from 'modules/accountmanagement_group/store';
import groupfeature from 'modules/group_feature/store';
//Department staff information
import staff_info from 'modules/staff_info/store';
import drs_staff from 'modules/staff_info/drs_staff/store';
import so_staff from 'modules/staff_info/so_staff/store';
import und_staff from 'modules/staff_info/und_staff/store';
import branch_network_staff from 'modules/staff_info/branch_network_staff/store';
import callcenter_staff from 'modules/staff_info/callcenter_staff/store';
import telesale_staff from 'modules/staff_info/telesale_staff/store';
import thirdparty_staff from 'modules/staff_info/thirdparty_staff/store';
import drs_detail from 'modules/staff_info/drs_detail/store';
import requestTicket from 'modules/staff_info/request_tickets/store';
import verifyTickets from 'modules/staff_info/verify_tickets/store';
import reportRequest from 'modules/staff_info/report_request/store';
import accountInfoReview from 'modules/staff_info/account_info_review/store';

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    global,
    login,
    register,
    users,
    forgotpassword,
    resetstatus,
    accountmanagement,
    accountmanagement_group,
    groupfeature,
    staff_info,
    drs_staff,
    so_staff,
    und_staff,
    branch_network_staff,
    callcenter_staff,
    telesale_staff,
    thirdparty_staff,
    drs_detail,
    requestTicket,
    verifyTickets,
    reportRequest,
    accountInfoReview
  },
  strict: process.env.NODE_ENV !== 'production'
});
