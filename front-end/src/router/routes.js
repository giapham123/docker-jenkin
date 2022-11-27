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
    name: NAMING.TERMINATION
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
    name: NAMING.RECEIPT_UPLOAD
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
];
