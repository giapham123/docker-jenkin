import __last from 'lodash/last';
import * as file from './file';

const randomElement = (arr = []) => {
  return arr[Math.floor(Math.random() * arr.length)];
};

const kebab = str => {
  return (str || '').replace(/([a-z])([A-Z])/g, '$1-$2').toLowerCase();
};

const removeExtention = filename => {
  if (
    filename != null &&
    filename != undefined &&
    typeof filename === 'string'
  ) {
    let parts = filename.split('.');
    return parts[0];
  }
  return '';
};

const getExtension = filename => {
  if (
    filename != null &&
    filename != undefined &&
    typeof filename === 'string'
  ) {
    let parts = filename.split('.');
    return __last(parts);
  }
  return '';
};

const toggleFullScreen = () => {
  let doc = window.document;
  let docEl = doc.documentElement;

  let requestFullScreen =
    docEl.requestFullscreen ||
    docEl.mozRequestFullScreen ||
    docEl.webkitRequestFullScreen ||
    docEl.msRequestFullscreen;
  let cancelFullScreen =
    doc.exitFullscreen ||
    doc.mozCancelFullScreen ||
    doc.webkitExitFullscreen ||
    doc.msExitFullscreen;

  if (
    !doc.fullscreenElement &&
    !doc.mozFullScreenElement &&
    !doc.webkitFullscreenElement &&
    !doc.msFullscreenElement
  ) {
    requestFullScreen.call(docEl);
  } else {
    cancelFullScreen.call(doc);
  }
};

export default {
  file,
  randomElement,
  toggleFullScreen,
  kebab,
  removeExtention,
  getExtension
};
