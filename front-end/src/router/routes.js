export default [
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
    path: '/',
    redirect: {
      path: '/accountmanagement'
    }
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
    path: '/accountmanagement',
    meta: {
      requiresAuth: true
    },
    name: 'Account_Management',
    component: () => import('modules/accountmanagement')
  },
  {
    path: '/accountmanagement_group',
    meta: {
      requiresAuth: true
    },
    name: 'Account Management on Group Permission',
    component: () => import('modules/accountmanagement_group')
  },
  {
    path: '/groupfeature',
    meta: {
      requiresAuth: true
    },
    name: 'Group Feature Management',
    component: () => import('modules/group_feature')
  },
  {
    path: '/staff_info/branch_network_staff',
    meta: {
      requiresAuth: true
    },
    name: 'Staff Information Of Branch Network Department',
    component: () => import('modules/staff_info/branch_network_staff')
  },
  {
    path: '/staff_info/drs_staff',
    meta: {
      requiresAuth: true
    },
    name: 'Staff Information Of DRS Department',
    component: () => import('modules/staff_info/drs_staff')
  },
  {
    path: '/staff_info/drs_detail',
    meta: {
      requiresAuth: true
    },
    name: 'Staff Information Of DRS Detail',
    component: () => import('modules/staff_info/drs_detail')
  },
  {
    path: '/staff_info/so_staff',
    meta: {
      requiresAuth: true
    },
    name: 'Staff Information Of Sale Operation Department',
    component: () => import('modules/staff_info/so_staff')
  },
  {
    path: '/staff_info/und_staff',
    meta: {
      requiresAuth: true
    },
    name: 'Staff Information Of Underwriting Department',
    component: () => import('modules/staff_info/und_staff')
  },
  {
    path: '/staff_info/callcenter_staff',
    meta: {
      requiresAuth: true
    },
    name: 'Staff Information Of Call Center Department',
    component: () => import('modules/staff_info/callcenter_staff')
  },
  {
    path: '/staff_info/telesale_staff',
    meta: {
      requiresAuth: true
    },
    name: 'Staff Information Of Telesale Department',
    component: () => import('modules/staff_info/telesale_staff')
  },
  {
    path: '/staff_info/thirdparty_staff',
    meta: {
      requiresAuth: true
    },
    name: 'Staff Information Of Third Party Department',
    component: () => import('modules/staff_info/thirdparty_staff')
  },
  {
    path: '/staff_info/request_tickets',
    meta: {
      requiresAuth: true
    },
    name: 'Request Ticket',
    component: () => import('modules/staff_info/request_tickets')
  },
  {
    path: '/staff_info/verify_tickets',
    meta: {
      requiresAuth: true
    },
    name: 'Verify Ticket',
    component: () => import('modules/staff_info/verify_tickets')
  },
  {
    path: '/staff_info/report_requests',
    meta: {
      requiresAuth: true
    },
    name: 'Report Request',
    component: () => import('modules/staff_info/report_request')
  },
  {
    path: '/staff_info/account_review',
    meta: {
      requiresAuth: true
    },
    name: 'Account Review',
    component: () => import('modules/staff_info/account_info_review')
  }
];
