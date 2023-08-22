import NAMING from './routes-naming';

export const BASE_ROUTES = [
  {
    path: '*',
    meta: {
      title: 'Not Found'
    },
    redirect: {
      path: '/404'
    }
  },
  {
    path: '/404',
    meta: {
      title: 'Not Found'
    },
    name: 'NotFound',
    component: () => import('modules/pages/NotFound.vue')
  },
  {
    path: '/401',
    meta: {
      title: 'Unanthorazed'
    },
    name: 'Unanthorazed',
    component: () => import('modules/pages/Unauthorized.vue')
  },
  {
    path: '/403',
    meta: {
      title: 'Access Denied'
    },
    name: 'AccessDenied',
    component: () => import('modules/pages/Deny.vue')
  },
  {
    path: '/500',
    meta: {
      title: 'Internal Error Server'
    },
    name: 'ServerError',
    component: () => import('modules/pages/Error.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('modules/login'),
    meta: {
      title: 'Login'
    }
  },
  {
    path: '/disbursal-tool/dashboard',
    meta: {
      requiresAuth: true
    },
    name: NAMING.DASHBOARD,
    component: () => import('modules/dashboard')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('modules/register')
  },
  {
    path: '/forgot-password',
    name: 'Forgotpassword',
    component: () => import('modules/forgotpassword')
  },
  {
    path: '/users',
    meta: {
      requiresAuth: true
    },
    name: 'Users',
    component: () => import('modules/users')
  },
  {
    path: '/reset',
    name: 'ResetStatus',
    component: () => import('modules/reset-status'),
    meta: {
      title: 'Reset Status'
    }
  },
  {
    path: '/',
    meta: {
      title: 'Dashboard',
      header: 'Dashboard',
      requiresAuth: true,
      menu: true,
      icon: 'home'
    },
    name: 'dashboard',
    component: () => import('modules/dashboard')
  }
];
export const PARENT_MENU = {
  PARENT_MENU_RECONCILING: [
    'acc20',
    'acc21',
    'acc22',
    'acc16',
    'acc17',
    'acc18',
    'acc19',
    'acc23',
    'acc24'
  ],
  PARENT_MENU_RECONCILING_MANUAL: [
    'acc16',
    'acc17',
    'acc18',
    'acc24',
    'acc23',
    'acc22'
  ],
  PARENT_MENU_RECONCILING_COLL: ['acc19', 'acc23'],
  TERMINATION: ['acc01', 'acc02', 'acc04', 'w_ext', 'acc11'],
  RECEIPT_UPLOAD: ['re01', 're02', 're03', 'acc03'],
  ADVANCE_BOOKING: ['acc09', 'acc10'],
  DISBUR_REPORT: ['acc12', 'acc13', 'acc14', 'acc15']
};

export const TERMI_ROUTES = [
  {
    path: '/termination',
    meta: {
      requiresAuth: true,
      menu: true,
      super: true,
      group: 'acc_01',
      title: 'Termination',
      header: 'Termination',
      icon: 'dashboard'
    },
    name: 'TERMINATION'
  },
  {
    path: '/check-termination-daily-report',
    component: () => import('modules/check-termination-daily-report'),
    meta: {
      requiresAuth: true,
      menu: true,
      group: 'acc_01',
      title: 'Check Termination Daily Report',
      header: 'Check Termination Daily Report'
    },
    name: NAMING.CHECK_TERMINATION_DAILY_REPORT
  },
  {
    path: '/termination-simulation-report',
    component: () => import('modules/accounting'),
    meta: {
      requiresAuth: true,
      menu: true,
      group: 'acc_01',
      title: 'Termination simulation report',
      header: 'Termination simulation report'
    },
    name: NAMING.ACCOUNTING
  },
  {
    path: '/termination-simulation-report-his',
    component: () => import('modules/accounting-his'),
    meta: {
      requiresAuth: true,
      menu: true,
      group: 'acc_01',
      title: 'Termination simulation report his',
      header: 'Termination simulation report his'
    },
    name: NAMING.ACCOUNTING_HIS
  },
  {
    path: '/writeoff-ext',
    component: () => import('modules/writeoff-ext'),
    meta: {
      requiresAuth: true,
      menu: true,
      group: 'acc_01',
      title: 'WriteOff EXT',
      header: 'WriteOff EXT'
    },
    name: NAMING.WRITEOFFEXT
  },

  {
    path: '/waveoffamount',
    component: () => import('modules/wave-off-amount'),
    meta: {
      requiresAuth: true,
      title: 'Waveoff Amount',
      menu: true,
      header: 'Waveoff Amount'
    },
    name: NAMING.WAVE_OFF_AMOUNT
  },
  {
    path: '/receipt-upload',
    meta: {
      requiresAuth: true,
      menu: true,
      super: true,
      group: 'acc_02',
      title: 'Receipt Upload',
      header: 'Receipt Upload',
      icon: 'upload_file'
    },
    name: 'RECEIPT_UPLOAD'
  },
  {
    path: '/upload-file',
    component: () => import('modules/upload-file'),
    meta: {
      requiresAuth: true,
      menu: true,
      group: 'acc_02',
      title: 'Receipt upload report',
      header: 'Receipt upload report'
    },
    name: NAMING.UPLOAD_FILE
  },
  {
    path: '/reject-upload-file',
    component: () => import('modules/reject-upload-file'),
    meta: {
      requiresAuth: true,
      menu: true,
      group: 'acc_02',
      title: 'Reject upload report',
      header: 'Reject upload report'
    },
    name: NAMING.REJECT_UPLOAD_FILE
  },
  {
    path: '/reject-upload-file-gl-sap',
    component: () => import('modules/reject-upload-file-gl-sap'),
    meta: {
      requiresAuth: true,
      menu: true,
      group: 'acc_02',
      title: 'Reject upload_SAP template',
      header: 'Reject upload_SAP template'
    },
    name: NAMING.REJECT_UPLOAD_FILE_GL_SAP
  },
  {
    path: '/termination-out-net-report',
    component: () => import('modules/out-net-report'),
    meta: {
      requiresAuth: true,
      menu: true,
      group: 'acc_02',
      title: 'OUT NET Report',
      header: 'OUT NET Report'
    },
    name: NAMING.OUT_NET_REPORT
  },
  {
    path: '/close-soldout',
    component: () => import('modules/close-soldout'),
    meta: {
      requiresAuth: true,
      title: 'Close Soldout',
      menu: true,
      header: 'Close Soldout'
    },
    name: NAMING.CLOSE_SOLDOUT
  },
  {
    path: '/schedule-bf-reduce-interest',
    component: () => import('modules/schedule-bf-reduce-interest'),
    meta: {
      requiresAuth: true,
      title: 'Schedule bf reduce interest',
      menu: true,
      header: 'Schedule bf reduce interest'
    },
    name: NAMING.SCHEDULE_BF_REDUCE_INTEREST
  },
  {
    path: '/schedule-bf-int-advance-booking',
    component: () => import('modules/schedule-bf-int-advance-booking'),
    meta: {
      requiresAuth: true,
      group: 'advance',
      title: 'Schedule bf Int advance booking',
      menu: true,
      header: 'Schedule bf Int advance booking'
    },
    name: NAMING.SCHEDULE_BF_INT_ADVANCE_BOOKING
  },
  {
    path: '/bank-statement',
    meta: {
      requiresAuth: true,
      menu: true,
      title: 'Bank Statement',
      header: 'Bank Statement',
      icon: 'dashboard'
    },
    name: NAMING.BANK_STATEMENT,
    component: () => import('modules/bank-statement')
  },
  {
    path: '/cas-repayment-schedule',
    meta: {
      requiresAuth: true,
      menu: true,
      title: 'Cas Repayment Schedule',
      header: 'Cas Repayment Schedule',
      icon: 'dashboard'
    },
    name: NAMING.CAS_REPAYMENT_SCHEDULE,
    component: () => import('modules/cas-repayment-schedule')
  },
  {
    path: '/advancebooking',
    meta: {
      requiresAuth: true,
      menu: true,
      super: true,
      group: 'advance',
      title: 'Advance Booking',
      header: 'Advance Booking',
      icon: 'dashboard'
    },
    name: 'ADVANCE_BOOKING'
  },
  {
    path: '/return-booking-case',
    meta: {
      requiresAuth: true,
      menu: true,
      group: 'advance',
      title: 'Return Booking Case',
      header: 'Return Booking Case',
      icon: 'dashboard'
    },
    name: NAMING.RETURN_BOOKING_CASE,
    component: () => import('modules/return-booking-case')
  },
  {
    path: '/termination-simulation-report-appid',
    meta: {
      requiresAuth: true,
      menu: true,
      group: 'acc_01',
      title: 'Termination Simulator Report-AppId',
      header: 'Termination Simulator Report-AppId',
      icon: 'dashboard'
    },
    name: NAMING.TERMINATION_REPORT_APP,
    component: () => import('modules/termination-report-app')
  },
  {
    path: '/disbursal-report',
    meta: {
      requiresAuth: true,
      menu: true,
      super: true,
      group: 'disbursalRpt',
      title: 'Disbursal Report',
      header: 'Disbursal Report',
      icon: 'dashboard'
    },
    name: 'DISBUR_REPORT'
  },
  {
    path: '/upload-bank-statement',
    meta: {
      requiresAuth: true,
      menu: true,
      group: 'disbursalRpt',
      title: 'Upload Bank Statement',
      header: 'Upload Bank Statement',
      icon: 'dashboard'
    },
    name: NAMING.UPLOAD_BANK_STATEMENT,
    component: () => import('modules/upload-bank-statement')
  },
  {
    path: '/detail-disbursal-report',
    meta: {
      requiresAuth: true,
      menu: true,
      group: 'disbursalRpt',
      title: 'Detail Disbursal Report',
      header: 'Detail Disbursal Report',
      icon: 'dashboard'
    },
    name: NAMING.DETAIL_DISBURSAL_REPORT,
    component: () => import('modules/detail-disbursal-report')
  },
  {
    path: '/daily-disbursal-report',
    meta: {
      requiresAuth: true,
      menu: true,
      group: 'disbursalRpt',
      title: 'Daily Disbursal Report',
      header: 'Daily Disbursal Report',
      icon: 'dashboard'
    },
    name: NAMING.DAILY_DISBURSAL_REPORT,
    component: () => import('modules/daily-disbursal-report')
  },
  {
    path: '/monthly-disbursal-report',
    meta: {
      requiresAuth: true,
      menu: true,
      group: 'disbursalRpt',
      title: 'Monthly Disbursal Report',
      header: 'Monthly Disbursal Report',
      icon: 'dashboard'
    },
    name: NAMING.MONTHLY_DISBURSAL_REPORT,
    component: () => import('modules/monthly-disbursal-report')
  },
  {
    path: '/sap-reconciling',
    meta: {
      requiresAuth: true,
      menu: true,
      super: true,
      group: 'sapReconciling',
      title: 'SAP reconciling',
      header: 'SAP reconciling',
      icon: 'dashboard'
    },
    name: 'PARENT_MENU_RECONCILING'
  },
  {
    path: '/sap-reconciling/manual-rec',
    meta: {
      requiresAuth: true,
      menu: true,
      isGroup: true,
      subGroup: 'sapReconcilingManual',
      group: 'sapReconciling',
      title: 'Manual reconciling',
      header: 'Manual reconciling',
      icon: 'dashboard'
    },
    name: 'PARENT_MENU_RECONCILING_MANUAL'
  },
  {
    path: '/sap-reconciling/coll-rec',
    meta: {
      requiresAuth: true,
      menu: true,
      isGroup: true,
      subGroup: 'sapReconcilingColl',
      group: 'sapReconciling',
      title: 'Collector reconciling',
      header: 'Collector reconciling',
      icon: 'dashboard'
    },
    name: 'PARENT_MENU_RECONCILING_COLL'
  },
  {
    path: '/upload-the-reconciling-result',
    meta: {
      requiresAuth: true,
      group: 'sapReconcilingManual',
      menu: true,
      title: 'Upload the reconciling result',
      header: 'Upload the reconciling result',
      icon: 'dashboard'
    },
    name: NAMING.SAP_UPLOAD_RECONCILING,
    component: () => import('modules/sap-upload-reconciling')
  },
  {
    path: '/gen-sap',
    meta: {
      requiresAuth: true,
      group: 'sapReconcilingManual',
      menu: true,
      title: ' Authorize Gen SAP',
      header: 'Authorize Gen SAP',
      icon: 'dashboard'
    },
    name: NAMING.GEN_SAP,
    component: () => import('modules/gen-sap')
  },
  {
    path: '/collector-transactions',
    meta: {
      requiresAuth: true,
      group: 'sapReconcilingColl',
      menu: true,
      title: ' Collector transactions',
      header: 'Collector transactions',
      icon: 'dashboard'
    },
    name: NAMING.COLL_TRANS,
    component: () => import('modules/coll-trans')
  },
  {
    path: '/collector-compare',
    meta: {
      requiresAuth: true,
      group: 'sapReconcilingColl',
      menu: true,
      title: 'Collector Compare',
      header: 'Collector Compare',
      icon: 'dashboard'
    },
    name: NAMING.COLL_COMPARE,
    component: () => import('modules/coll-compare')
  },

  {
    path: '/pending-disbursement',
    meta: {
      requiresAuth: true,
      group: 'sapReconcilingManual',
      menu: true,
      title: 'PD AgreementIDs',
      header: 'PD AgreementIDs',
      icon: 'dashboard'
    },
    name: NAMING.PENDING_DISBURSEMENT,
    component: () => import('modules/pending-disbursement')
  },
  {
    path: '/back-date-wo',
    meta: {
      requiresAuth: true,
      group: 'sapReconcilingManual',
      menu: true,
      title: 'Back Date WO',
      icon: 'dashboard'
    },
    name: NAMING.BACK_DATE_WO,
    component: () => import('modules/back-date-wo')
  },

  {
    path: '/view-manual-entries',
    meta: {
      requiresAuth: true,
      group: 'sapReconcilingManual',
      menu: true,
      title: 'View manual Entries',
      header: 'View manual Entries',
      icon: 'dashboard'
    },
    name: NAMING.VIEW_MAP_SAP_GL,
    component: () => import('modules/view-map')
  },
  {
    path: '/view-finnone-entries',
    meta: {
      requiresAuth: true,
      group: 'sapReconciling',
      menu: true,
      title: 'View Finnone Entries',
      header: 'View Finnone Entries',
      icon: 'dashboard'
    },
    name: NAMING.VIEW_FINNONE_ENTRIES,
    component: () => import('modules/view-finnone-entries')
  },
  {
    path: '/view-sap-entries',
    meta: {
      requiresAuth: true,
      group: 'sapReconciling',
      menu: true,
      title: 'View SAP Entries',
      header: 'View SAP Entries',
      icon: 'dashboard'
    },
    name: NAMING.VIEW_SAP_ENTRIES,
    component: () => import('modules/view-sap-entries')
  }
];
