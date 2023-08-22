export const viewPDFFile = data => {
  viewFileInNewTab(data, 'application/pdf;charset=UTF-8');
};

export const viewFileInNewTab = (data, type) => {
  let buffer = base64ToArrayBuffer(data);
  var file = new Blob([buffer], { type });
  var fileURL = URL.createObjectURL(file);
  window.open(fileURL, '_blank', '');
};

export const base64ToArrayBuffer = base64 => {
  var binary_string = window.atob(base64);
  var len = binary_string.length;
  var bytes = new Uint8Array(len);
  for (var i = 0; i < len; i++) {
    bytes[i] = binary_string.charCodeAt(i);
  }
  return bytes.buffer;
};

export const getFileFromBase64 = (data, type) => {
  let buffer = base64ToArrayBuffer(data);
  return new Blob([buffer], { type });
};
