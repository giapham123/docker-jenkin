<template>
  <v-menu
    ref="menu"
    :close-on-content-click="false"
    v-model="menu"
    :nudge-right="40"
    lazy
    transition="scale-transition"
    offset-y
    full-width
    min-width="290px"
  >
    <v-text-field
      slot="activator"
      v-model="show"
      :label="label"
      :prepend-icon="icon"
      :disabled="disabledDate"
      readonly
    />
    <v-date-picker
      v-model="picker"
      :allowed-dates="allowedDate"
      :color="color"
      :disabled="disabledParam"
      :min="minPicker"
      no-titl
      scrollable
      @input="pickDate"
    />
  </v-menu>
</template>

<script>
export default {
  props: {
    disabledDate: {
      type: Boolean,
      default: false
    },
    disabledParam: {
      type: Boolean,
      default: false
    },
    value: {
      type: String,
      default: new Date().toLocaleDateString('en-US'),
      required: true
    },
    minPicker: {
      type: String,
      default: null
    },
    label: {
      type: String,
      default: '',
      required: false
    },

    icon: {
      type: String,
      default: '',
      required: false
    },

    greater: {
      type: String,
      default: '',
      required: false
    },

    smaller: {
      type: String,
      default: '',
      required: false
    },

    equal: {
      type: Boolean,
      default: true,
      required: false
    },

    color: {
      type: String,
      default: '#00695c',
      required: false
    }
  },

  data() {
    return {
      menu: false,

      show: '',

      picker: ''
    };
  },

  watch: {
    value() {
      this.show = this.value;
      this.picker = this.parseDate(this.value);
    },

    picker() {
      this.show = this.formatDate(this.picker);
      this.$emit('input', this.show);
    }
  },

  created() {
    this.show = this.value;
    this.picker = this.parseDate(this.value);
  },

  methods: {
    pickDate() {
      this.menu = false;
    },

    formatDate(date) {
      if (!date) return null;
      const [year, month, day] = date.split('-');
      return `${month}/${day}/${year}`;
    },

    parseDate(date) {
      if (!date) return null;
      const [month, day, year] = date.split('/');
      return `${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')}`;
    },

    isValidDate(date) {
      let result = new Date(date);
      if (result != 'Invalid Date') {
        return true;
      }

      return false;
    },

    allowedDate(val) {
      let more = false;
      let less = false;

      if (this.greater == '') {
        more = true;
      } else {
        if (this.isValidDate(this.greater)) {
          more = this.equal
            ? this.createDate(val) >= this.createDate(this.greater)
            : this.createDate(val) > this.createDate(this.greater);
        } else {
          more = false;
        }
      }

      if (this.smaller == '') {
        less = true;
      } else {
        if (this.isValidDate(this.smaller)) {
          less = this.equal
            ? this.createDate(val) <= this.createDate(this.smaller)
            : this.createDate(val) < this.createDate(this.smaller);
        } else {
          less = false;
        }
      }
      return more && less;
    },

    createDate(date) {
      let [month, day, year] = date.split('/');
      if (year == undefined) {
        [year, month, day] = date.split('-');
      }
      return new Date(Date.UTC(year, month, day));
    }
  }
};
</script>
