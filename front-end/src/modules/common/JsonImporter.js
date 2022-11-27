import XLSX from 'xlsx';
import moment from 'moment';
import _ from 'lodash';

export const DATE_DEFAULT_FORMAT = {
  INPUT: 'DD/MM/YYYY',
  OUTPUT: 'DD/MM/YYYY hh:mm:ss a'
};

export default class Importer {
  /**
   * Return MAP contain json data corresponding worksheet.
   * @param FILE file
   * @param STRING ARRAY wooksheets
   */
  static async readXLSX(file, wooksheets, { rABS } = {}) {
    let reader;
    if (rABS) {
      reader = new XLSX_Reader(true);
    } else {
      reader = new XLSX_Reader();
    }

    return await reader.readFile(file, wooksheets);
  }

  static async readXLSX_2(file, wooksheets, { rABS } = {}) {
    let reader;
    if (rABS) {
      reader = new XLSX_Reader_2(true);
    } else {
      reader = new XLSX_Reader_2();
    }

    return await reader.readFile(file, wooksheets);
  }

  /**
   * Return json data of the first sheet.
   * @param FILE file
   */
  static async readXLSXAllSheet(file, { rABS } = {}) {
    let reader;
    if (rABS) {
      reader = new XLSX_Reader(true);
    } else {
      reader = new XLSX_Reader();
    }

    return await reader.readFile(file, []);
  }

  /**
   * Rename property name for each item in data array
   * @param ARRAY data
   * @param OBJECT properties
   */
  static correctJsonPropertyName(data, properties) {
    try {
      if (Object.keys(properties).length == 0 || data.length == 0)
        return {
          success: false,
          message: 'Wrong arguments'
        };
    } catch (e) {
      return {
        success: false,
        message: 'Wrong arguments'
      };
    }

    let result = [];
    let dateFormatHolder = {};
    for (let index in data) {
      let element = data[index];
      let new_element = {};
      for (let property_name in properties) {
        if (element[properties[property_name].name] != undefined) {
          new_element[property_name] = element[properties[property_name].name];
        } else {
          new_element[property_name] = '';
        }
      }
      let valid = Ulti.validate(new_element, properties, dateFormatHolder);
      if (valid.success) {
        result.push(valid.data);
      } else {
        return valid;
      }
    }

    return {
      success: true,
      data: result
    };
  }
}

class Ulti {
  static validate(data, properties, dateFormatHolder) {
    let result = {};
    for (let property_name in properties) {
      if (_.isNil(data[property_name])) {
        return {
          success: false,
          message: 'Imported file is invalid format. Miss attribute '
        };
      }
      let raw_data = _.trim(data[property_name]);
      let type = properties[property_name].type;
      if (!type) {
        type = 'String';
      }

      switch (type) {
        case 'String':
          // Ignore
          result[property_name] = data[property_name];
          break;
        case 'Number':
          if (raw_data != '' || raw_data != null) {
            if (!Number.isNaN(Number(raw_data.replace(/,/g, '')))) {
              result[property_name] = Number(raw_data.replace(/,/g, ''));
            } else {
              return {
                success: false,
                message:
                  'The value of collumn [' +
                  properties[property_name].name +
                  ' <' +
                  (raw_data == '' ? 'EMPTY VALUE' : raw_data) +
                  '>] must be NUMBER'
              };
            }
          }
          break;
        case 'Boolean':
          if (raw_data == 'true' || raw_data == 'false') {
            result[property_name] = raw_data == 'true';
          } else {
            return {
              success: false,
              message:
                'The value of collumn [' +
                properties[property_name].name +
                ' <' +
                (raw_data == '' ? 'EMPTY VALUE' : raw_data) +
                '>] must be TRUE or FALSE'
            };
          }
          break;
        case 'Date':
          Ulti.readDateFormat(
            property_name,
            properties[property_name],
            dateFormatHolder
          );
          if (_.trim(raw_data) === '') {
            result[property_name] = null;
          } else {
            if (
              !Ulti.formatDate(
                result,
                property_name,
                dateFormatHolder,
                raw_data
              )
            ) {
              return {
                success: false,
                message:
                  'The value of collumn [' +
                  properties[property_name].name +
                  ' <' +
                  (raw_data == '' ? 'EMPTY VALUE' : raw_data) +
                  '>] can not understand as Date value'
              };
            }
          }
          break;
        default:
          // Ignore
          result[property_name] = data[property_name];
          break;
      }
    }
    return { success: true, data: result };
  }

  static formatDate(result, column, dateFormatHolder, raw_data) {
    let format = dateFormatHolder[column];
    if (_.isNil(format)) {
      dateFormatHolder[column] = {
        inFormat: [DATE_DEFAULT_FORMAT.INPUT],
        outFormat: DATE_DEFAULT_FORMAT.OUTPUT
      };
    }
    let dateVal = moment(raw_data, format.inFormat);
    if (dateVal.isValid()) {
      result[column] = dateVal.format(format.outFormat);
      return true;
    } else {
      return false;
    }
  }

  static readDateFormat(column, properties, dateFormatHolder) {
    if (_.isNil(dateFormatHolder[column])) {
      let inFormat = [DATE_DEFAULT_FORMAT.INPUT];
      let outFormat = DATE_DEFAULT_FORMAT.OUTPUT;

      if (_.has(properties, 'format')) {
        if (!_.isNil(properties.format.input)) {
          if (_.isArray(properties.format.input)) {
            inFormat = _.concat(inFormat, properties.format.input);
          } else if (_.isString(properties.format.input)) {
            inFormat.push(properties.format.input);
          }
        }

        if (
          _.isString(properties.format.output) &&
          properties.format.output !== ''
        ) {
          outFormat = properties.format.output;
        }
      }

      dateFormatHolder[column] = { inFormat, outFormat };
    }
  }
}

class XLSX_Reader {
  constructor(rABS) {
    this.rABS = rABS;
  }

  async readFile(file, wooksheets) {
    return await this.fileConvertToWorkbook(file)
      .then(workbook => {
        let result_data = {};
        if (wooksheets.length == 0) {
          for (let index in workbook.SheetNames) {
            let xlsxArr = XLSX.utils.sheet_to_json(
              workbook.Sheets[workbook.SheetNames[index]]
            );
            result_data[workbook.SheetNames[index]] = xlsxArr;
          }
        } else {
          for (let index in wooksheets) {
            let sheet_name = wooksheets[index];
            let xlsxArr = XLSX.utils.sheet_to_json(workbook.Sheets[sheet_name]);
            if (xlsxArr) {
              result_data[sheet_name] = xlsxArr;
            }
          }
        }
        return {
          success: true,
          data: result_data
        };
      })
      .catch(err => {
        return {
          sucess: false,
          message: err
        };
      });
  }

  fileConvertToWorkbook(file) {
    let reader = new FileReader();
    let fixdata = data => {
      let o = '',
        l = 0,
        w = 10240;
      for (; l < data.byteLength / w; ++l) {
        o += String.fromCharCode.apply(
          null,
          new Uint8Array(data.slice(l * w, l * w + w))
        );
      }
      o += String.fromCharCode.apply(null, new Uint8Array(data.slice(l * w)));
      return o;
    };
    return new Promise((resolve, reject) => {
      try {
        reader.onload = renderEvent => {
          let data = renderEvent.target.result;
          if (this.rABS) {
            /* if binary string, read with type 'binary' */
            resolve(XLSX.read(data, { type: 'binary' }));
          } else {
            /* if array buffer, convert to base64 */
            let arr = fixdata(data);
            resolve(XLSX.read(btoa(arr), { type: 'base64' }));
          }
        };
        reader.onerror = error => {
          reject(error);
        };
        if (this.rABS) {
          reader.readAsBinaryString(file);
        } else {
          reader.readAsArrayBuffer(file);
        }
      } catch (error) {
        reject(error);
      }
    });
  }
}

class XLSX_Reader_2 {
  constructor(rABS) {
    this.rABS = rABS;
  }

  async readFile(file, wooksheets) {
    return await this.fileConvertToWorkbook(file)
      .then(workbook => {
        let result_data = {};
        if (wooksheets.length == 0) {
          for (let index in workbook.SheetNames) {
            let xlsxArr = XLSX.utils.sheet_to_json(
              workbook.Sheets[workbook.SheetNames[index]]
            );
            result_data[workbook.SheetNames[index]] = xlsxArr;
          }
        } else {
          for (let index in wooksheets) {
            let sheet_name = wooksheets[index];
            let xlsxArr = XLSX.utils.sheet_to_json(
              workbook.Sheets[sheet_name],
              { range: 5 }
            );
            if (xlsxArr) {
              result_data[sheet_name] = xlsxArr;
            }
          }
        }
        let result_data_2 = result_data;
        result_data_2.sheet.length = result_data_2.sheet.length - 2;
        // for (let i = 0; i < result_data.sheet.length - 2; i++) {
        //   result_data_2.sheet[i] = result_data.sheet[i];
        // }
        return {
          success: true,
          data: result_data_2
        };
      })
      .catch(err => {
        return {
          sucess: false,
          message: err
        };
      });
  }

  fileConvertToWorkbook(file) {
    let reader = new FileReader();
    let fixdata = data => {
      let o = '',
        l = 0,
        w = 10240;
      for (; l < data.byteLength / w; ++l) {
        o += String.fromCharCode.apply(
          null,
          new Uint8Array(data.slice(l * w, l * w + w))
        );
      }
      o += String.fromCharCode.apply(null, new Uint8Array(data.slice(l * w)));
      return o;
    };
    return new Promise((resolve, reject) => {
      try {
        reader.onload = renderEvent => {
          let data = renderEvent.target.result;
          if (this.rABS) {
            /* if binary string, read with type 'binary' */
            resolve(XLSX.read(data, { type: 'binary' }));
          } else {
            /* if array buffer, convert to base64 */
            let arr = fixdata(data);
            resolve(XLSX.read(btoa(arr), { type: 'base64' }));
          }
        };
        reader.onerror = error => {
          reject(error);
        };
        if (this.rABS) {
          reader.readAsBinaryString(file);
        } else {
          reader.readAsArrayBuffer(file);
        }
      } catch (error) {
        reject(error);
      }
    });
  }
}
