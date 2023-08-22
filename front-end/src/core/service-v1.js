import axios from 'axios';

import { AUTH_HEADER_KEY } from 'core/constants';

export default class Service {
  static requestInterceptors;

  static setToken(token) {
    axios.defaults.headers.common[AUTH_HEADER_KEY] = `${token}`;
  }

  static removeToken() {
    axios.defaults.headers.common[AUTH_HEADER_KEY] = undefined;
  }

  static interceptors({ request }) {
    if (request) this.requestInterceptors = request;
  }

  /**
   * Creates an instance of Service.
   *
   * @memberOf Service
   */
  constructor(namespace) {
    // Accept */*
    axios.defaults.headers.common['Accept'] = '*/*';
    const endpoint = process.env.VUE_APP_SERVICE_ENDPOINT || '';
    const baseURL = endpoint + (namespace ? `/${namespace}/` : '/');
    this.axios = axios.create({
      baseURL,
      responseType: 'json'
    });
  }

  withHeader(headers) {
    this.headers = headers;
    return this;
  }

  toQueryString(obj) {
    const parts = [];
    for (let i in obj) {
      if (obj.hasOwnProperty(i)) {
        parts.push(encodeURIComponent(i) + '=' + encodeURIComponent(obj[i]));
      }
    }
    return parts.join('&');
  }

  /**
   * Call a service action via REST API
   *
   * @param {any} action  name of action
   * @param {any} params  parameters to request
   * @returns  {Promise}
   *
   * @memberOf Service
   */
  async rest(action, params, options = {}, isRawResponse) {
    if (Service.requestInterceptors) {
      this.axios.interceptors.request.use(Service.requestInterceptors);
    }
    const { method = 'post', headers = {}, ...rest } = options;
    var response = {};
    try {
      const opts = {
        method: method,
        data: params,
        headers: {
          ...(this.headers || {}),
          ...headers
        },
        ...rest
      };
      response = await this.axios.request(action, opts);
    } catch (err) {
      response = err;
    }
    return isRawResponse ? response : this.readResponse(response);
  }

  get(action, params, options = {}, isRawResponse = false) {
    const query = this.toQueryString(params);
    const path = query ? `${action}?${query}` : action;
    return this.rest(
      path,
      {},
      {
        method: 'get',
        ...options
      },
      isRawResponse
    );
  }

  post(action, params, options = {}, isRawResponse = false) {
    return this.rest(
      action,
      params,
      {
        method: 'post',
        ...options
      },
      isRawResponse
    );
  }

  put(action, params, options = {}, isRawResponse = false) {
    return this.rest(
      action,
      params,
      {
        method: 'put',
        ...options
      },
      isRawResponse
    );
  }

  delete(action, params, options = {}, isRawResponse = false) {
    return this.rest(
      action,
      params,
      {
        method: 'delete',
        ...options
      },
      isRawResponse
    );
  }

  readResponse(resp) {
    try {
      if (resp.response != undefined) {
        resp = resp.response;
      }
      switch (resp.status) {
        case 102:
          return {
            success: false,
            data: null,
            message: 'Can not connect to Server API.',
            http_status: 102
          };

        case 200:
          return resp.data;

        case 304:
          return {
            success: false,
            data: null,
            message: 'Server API do not accept CORS from this address.',
            http_status: 304
          };

        case 401:
          return {
            success: false,
            data: null,
            message: 'You do not have permision on API.',
            http_status: 401
          };

        case 403:
          return {
            success: false,
            data: null,
            message: 'Must be authenticate befor accessing this API.',
            http_status: 403
          };

        case 404:
          return {
            success: false,
            data: null,
            message: 'The request URL do not exist on server API.',
            http_status: 404
          };

        case 500:
          return {
            success: false,
            data: null,
            message:
              'Has an error 500 for this request. Refer admin to resolve.',
            http_status: 500
          };
        default:
          return {
            success: false,
            data: resp,
            message:
              'Has unknown error while try to connect server. Please try again!'
          };
      }
    } catch (e) {
      return {
        success: false,
        data: null,
        message:
          'Has unknown error while try to connect server. Please try again!'
      };
    }
  }
}
