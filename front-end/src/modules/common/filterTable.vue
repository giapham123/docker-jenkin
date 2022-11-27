<template>
  <div class="filter-table">
    <v-data-table
      :headers="headers"
      :items="data_source"
      :rows-per-page-items="page_size"
      :loading="loading"
      :pagination.sync="pagination"
    >
      <template slot="headers" slot-scope="props">
        <tr class="text-uppercase">
          <template v-for="header in props.headers">
            <th
              v-if="!setting_header || no_hiddens[header.value] == true"
              :key="header.text"
              :class="[
                'column sortable',
                pagination.descending ? 'desc' : 'asc',
                header.value === pagination.sortBy ? 'active' : ''
              ]"
              @click="changeSort(header.value, header.sortable)"
            >
              {{ header.text }}
              <v-icon v-if="header.sortable" small>arrow_upward</v-icon>
            </th>
          </template>
        </tr>
        <tr class="search_header">
          <template v-for="header in props.headers">
            <th
              v-if="!setting_header || no_hiddens[header.value] == true"
              :key="header.value"
              :style="calcWidthHeader(header)"
            >
              <v-text-field
                v-if="header.type === 'string' || header.type === 'number'"
                v-model="search_val[header.value].value"
                :append-outer-icon="search_val[header.value].operation.icon"
                @click:append-outer="openOperationList(header.value, $event)"
                @input="filterColunmOnTable(header.value)"
              />
              <!-- HangNguyen- 20191024- Add type dropdow filter  -->
              <v-select
                v-else-if="header.type === 'dropdown'"
                :items="['ALL', 'N/A', 'No', 'Yes']"
                v-model="search_val[header.value].value"
                label=""
                @input="filterColunmOnTable(header.value)"
              ></v-select>
              <!-- End HangNguyen- 20191024- Add type dropdow filter-->
              <checkbox
                v-else-if="header.type === 'bool'"
                v-model="search_val[header.value].value"
                @change="filterColunmOnTable(header.value)"
              />
              <v-menu
                v-else-if="header.type === 'date'"
                ref="${date_menu[header.value]}"
                :close-on-content-click="false"
                v-model="date_menu[header.value]"
                :nudge-right="40"
                lazy
                transition="scale-transition"
                offset-y
                full-width
                min-width="290px"
              >
                <v-text-field
                  slot="activator"
                  v-model="search_val[header.value].value"
                  readonly
                />
                <v-date-picker
                  v-model="search_val[header.value].value"
                  no-title
                  color="#00695c"
                  @input="
                    date_menu[header.value] = false;
                    filterColunmOnTable(header.value);
                  "
                >
                  <v-spacer />
                  <v-btn
                    flat
                    color="#00695c"
                    @click="
                      date_menu[header.value] = false;
                      search_val[header.value].value = '';
                      filterColunmOnTable(header.value);
                    "
                  >
                    Clear
                  </v-btn>
                </v-date-picker>
              </v-menu>
            </th>
          </template>
        </tr>
      </template>
      <template slot="items" slot-scope="props">
        <slot :item="props.item" name="tbody" />
      </template>
      <template slot="pageText" slot-scope="props">
        From {{ props.pageStart }} to {{ props.pageStop }} of
        {{ props.itemsLength }}
      </template>
    </v-data-table>
    <v-menu
      v-model="menu_operations.show"
      :position-x="menu_operations.x"
      :position-y="menu_operations.y"
      transition="slide-x-transition"
      absolute
      right
    >
      <v-list class="menu-v-list">
        <v-list-tile
          v-for="(item, index) in menu_operations.options"
          :key="index"
          :class="[menu_operations.current.code == item.code ? 'active' : '']"
          avatar
          @click="chooseOperation(item)"
        >
          <v-list-tile-action>
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-tile-action>
          <v-list-tile-content>
            <v-list-tile-title>{{ item.text }}</v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>
      </v-list>
    </v-menu>
  </div>
</template>

<script>
import _ from 'lodash';
import Checkbox from './checkbox';
import { FILTER_ULTIS, SUPPORT_TYPE } from './FilterOperations';
import FILTER_OPERATIONS from './FilterOperations';

export default {
  components: {
    Checkbox
  },

  props: {
    headers: {
      type: Array,
      default() {
        return [];
      },
      required: true
    },

    data: {
      type: Array,
      default() {
        return [];
      },
      required: true
    },

    setting_header: {
      type: Boolean,
      default: true
    },

    primary: {
      type: String,
      default: ''
    },

    no_hiddens: {
      type: Object,
      default() {
        return {};
      }
    },

    page_size: {
      type: Array,
      default() {
        return [10, 20, 50];
      },
      required: false
    },

    sort: {
      type: String,
      default: '',
      required: false
    },

    loading: {
      type: Boolean,
      default: false,
      required: false
    }
  },

  data() {
    return {
      date_menu: {},

      search_val: {},

      search_conditions: [],

      pagination: {
        descending: false,
        sortBy: ''
      },

      data_source: [],

      menu_operations: {
        show: false,
        x: 0,
        y: 0,
        current: {},
        column: '',
        options: []
      }
    };
  },

  watch: {
    sort() {
      this.pagination.sortBy = this.sort;
    },

    data() {
      this.filter();
    },

    no_hiddens() {
      this.filter();
    }
  },

  created() {
    if (_.isNil(this.headers)) {
      this.headers = [];
    } else {
      this.headers.forEach(el => {
        let type = _.toUpper(el.type);
        if (
          !_.isNil(type) &&
          SUPPORT_TYPE.includes(type) &&
          !_.isNil(el.value)
        ) {
          this.search_val[el.value] = {
            value: '',
            key: this.primary,
            type,
            column: el.value,
            results: [],
            operation: FILTER_OPERATIONS.DEFAULTS[type]
          };
        }
      });
    }

    this.$bus.$on('ft-refresh', () => this.filter());
  },

  methods: {
    changeSort(column, sortable) {
      if (sortable) {
        if (this.pagination.sortBy === column) {
          this.pagination.descending = !this.pagination.descending;
        } else {
          this.pagination.sortBy = column;
          this.pagination.descending = false;
        }
      }
    },

    calcWidthHeader(header) {
      if (
        _.isString(header.width) &&
        header.width.length > 2 &&
        _.endsWith(header.width, 'px')
      ) {
        return {
          'min-width': header.width
        };
      }
    },

    openOperationList(column, event) {
      event.preventDefault();
      this.menu_operations = {
        show: false,
        column,
        current: this.search_val[column].operation,
        options: FILTER_OPERATIONS[this.search_val[column].type]
      };
      this.menu_operations.x = event.clientX;
      this.menu_operations.y = event.clientY;
      this.$nextTick(() => {
        this.menu_operations.show = true;
      });
    },

    chooseOperation(operation) {
      let column = this.menu_operations.column;
      if (_.isString(column) && column != '') {
        this.search_val[column].operation = operation;
        this.filterColunmOnTable(column);
      }
    },

    filter() {
      _.forIn(this.search_val, (value, column) => {
        this.filterColunm(column);
      });
      this.getResult();
    },

    filterColunm(col) {
      if (this.setting_header && this.no_hiddens[col] != true) return;
      let condition = this.search_val[col];
      this.search_val[col].results = [];
      if (!FILTER_ULTIS.ignore[condition.type](condition.value)) {
        this.data.forEach(el => {
          if (!_.isNil(el[col])) {
            FILTER_ULTIS.filter(this.search_val[col], el);
          }
        });
      }
    },

    filterColunmOnTable(col) {
      this.filterColunm(col);
      this.getResult();
    },

    getResult() {
      let accepts = null;
      _.forIn(this.search_val, filter => {
        if (this.setting_header && this.no_hiddens[filter.column] != true)
          return;
        if (filter.value == 'ALL') filter.value = '';
        if (!FILTER_ULTIS.ignore[filter.type](filter.value)) {
          if (accepts == null) {
            accepts = filter.results;
            return;
          }

          accepts = _.intersection(accepts, filter.results);
        }
      });
      if (accepts != null) {
        this.data_source = this.data.filter(el =>
          _.includes(accepts, el[this.primary])
        );
      } else {
        this.data_source = this.data;
      }
      this.$emit(
        'first',
        this.data_source.length > 0 ? this.data_source[0] : {}
      );
    }
  }
};
</script>

<style>
.filter-table tbody tr {
  cursor: pointer;
}

.filter-table td .v-input--checkbox {
  justify-content: center;
}

.filter-table .search_header .v-text-field {
  font-weight: 400;
  font-size: 13px;
}

.filter-table .search_header .v-input__append-outer {
  margin-left: 0;
}

.filter-table .search_header .v-input__icon--append-outer .v-icon {
  font-size: 12px;
}

.menu-v-list {
  max-height: 240px;
  overflow-y: overlay;
  padding: 0;
}

.menu-v-list .v-list__tile {
  height: 40px;
  padding-left: 0 !important;
}

.menu-v-list .active .v-list__tile {
  background-color: #f1f1f1;
}

.menu-v-list .active .v-icon,
.menu-v-list .active .v-list__tile__title,
.menu-v-list .v-list__tile:hover .v-icon,
.menu-v-list .v-list__tile:hover .v-list__tile__title {
  color: #fb8c00 !important;
}

.menu-v-list .v-list__tile__action {
  min-width: unset;
  justify-content: center;
  width: 40px;
}

.menu-v-list .v-icon,
.menu-v-list .v-list__tile__title {
  font-size: 13px !important;
}
</style>
