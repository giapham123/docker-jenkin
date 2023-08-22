<template>
  <v-dialog v-model="dialog" width="50%">
    <v-form ref="form" v-model="valid" class="max_content">
      <v-card class="home-app-wrapper popup max_content">
        <v-layout class="header white--text no_margin">
          <v-flex md5>
            <v-card-title class="white--text title">
              Information Account Profile
            </v-card-title>
          </v-flex>
          <v-flex md7>
            <v-layout row align-center justify-end fill-height>
              <v-btn flat class="popup_btn icon" @click="handleChangePassword">
                <v-icon color="#00695c">save</v-icon>
                <p class="no_margin">SAVE</p>
              </v-btn>
              <v-btn flat icon @click="emitCloseState">
                <v-icon>close</v-icon>
              </v-btn>
            </v-layout>
          </v-flex>
        </v-layout>
        <v-card-text class="body overflow_hidden">
          <v-card class="no_shaddow">
            <v-container grid-list-md>
              <v-layout row wrap>
                <v-flex md4>
                  <v-text-field
                    v-model="account.username"
                    prepend-icon="perm_identity"
                    label="User Name"
                    readonly
                  />
                </v-flex>
                <v-flex md8>
                  <v-text-field
                    v-model="account.fullname"
                    label="Full Name"
                    readonly
                  />
                </v-flex>
              </v-layout>
              <v-layout row wrap>
                <v-flex md12>
                  <v-text-field
                    ref="password"
                    v-model="account.password"
                    :rules="passwordRules"
                    type="password"
                    label="Password"
                    prepend-icon="lock"
                    required
                    @keypress.enter="handleChangePassword"
                  />
                </v-flex>
              </v-layout>

              <v-layout row wrap>
                <v-flex md12>
                  <v-text-field
                    ref="newPassword"
                    v-model="account.newPassword"
                    :rules="newPasswordRules"
                    type="password"
                    label="New Password"
                    prepend-icon="lock"
                    required
                    @keypress.enter="handleChangePassword"
                  />
                </v-flex>
              </v-layout>

              <v-layout row wrap>
                <v-flex md12>
                  <v-text-field
                    ref="confirmedPassword"
                    v-model="account.confirmedPassword"
                    :rules="confirmRules"
                    type="password"
                    label="Confirm Password"
                    prepend-icon="lock"
                    required
                    @keypress.enter="handleChangePassword"
                  />
                </v-flex>
              </v-layout>
              <v-layout row wrap>
                <v-flex md4>
                  <v-text-field
                    v-model="account.group"
                    label="Group"
                    readonly
                  />
                </v-flex>
                <v-flex md4>
                  <v-text-field v-model="account.team" label="Team" readonly />
                </v-flex>
                <v-flex md4>
                  <v-text-field
                    v-model="account.department"
                    label="Department"
                    readonly
                  />
                </v-flex>
              </v-layout>
              <v-layout row wrap>
                <v-flex md4>
                  <v-text-field
                    v-model="account.level"
                    label="Level"
                    readonly
                  />
                </v-flex>
                <v-flex md4>
                  <v-text-field v-model="account.area" label="Area" readonly />
                </v-flex>
                <v-flex md4>
                  <v-text-field
                    v-model="account.positionCompanyId"
                    label="Position Company"
                    readonly
                  />
                </v-flex>
              </v-layout>
            </v-container>
          </v-card>
        </v-card-text>
      </v-card>
    </v-form>
  </v-dialog>
</template>

<script>
import { mapGetters, mapActions } from 'vuex';
import SHA256 from 'sha256';

export default {
  props: {
    show: {
      type: Boolean,
      default: true
    }
  },

  data() {
    return {
      account: {
        username: '',
        password: '',
        newPassword: '',
        comfirmedPassword: '',
        fullname: '',
        group: '',
        level: '',
        positionCompanyId: '',
        team: '',
        department: '',
        area: ''
      },
      valid: true,

      dialog: false,
      passwordRules: [v => !!v || 'Password is required'],
      newPasswordRules: [
        v => !!v || 'New password is required',
        v =>
          v != this.account.password || 'New password must be different to old',
        v => (v != undefined && v.length >= 8) || 'Minimum 8 characters',
        v => /[A-Z]+/.test(v) || 'At least 1 upper case character',
        v => /[0-9]+/.test(v) || 'At least 1 digit',
        v =>
          /[#?!@$%^&*-+.]+/.test(v) ||
          'At least special charater in #?!@$%^&*-+.'
      ],
      confirmRules: [
        v =>
          v == this.account.newPassword ||
          'Confirmed value do not match new password'
      ]
    };
  },
  computed: {
    ...mapGetters('login', ['me'])
  },

  watch: {
    show(value) {
      this.dialog = value;
      if (this.dialog) {
        this.loadData();
      }
    },

    dialog() {
      if (!this.dialog) {
        this.emitCloseState();
      }
    }
  },

  created() {
    this.dialog = this.show;
    this.loadData();
  },

  methods: {
    ...mapActions('login', ['changePassword']),
    ...mapActions('global', ['setLoading']),

    emitCloseState() {
      this.$emit('close', false);
      this.dialog = false;
    },

    loadData() {
      this.account.username = this.me.account_id;
      this.account.fullname = this.me.fullname;
      this.account.area = this.me.area;
      this.account.department = this.me.department;
      this.account.group = this.me.group;
      this.account.level = this.me.level;
      this.account.positionCompanyId = this.me.positionCompanyId;
      this.account.team = this.me.team;
    },

    validPassword() {
      let password = this.account.newPassword;

      if (password.length < 8) return false;

      if (!/[A-Z]+/.test(password)) return false;

      if (!/[0-9]+/.test(password)) return false;

      if (!/[#?!@$%^&*-+.]+/.test(password)) return false;

      return true;
    },

    async handleChangePassword() {
      if (this.account.password == '') {
        this.$refs.password.focus();
        return;
      }

      if (
        this.account.newPassword == '' ||
        this.account.password == this.account.newPassword ||
        !this.validPassword()
      ) {
        this.$refs.newPassword.focus();
        return;
      }

      if (this.account.newPassword != this.account.confirmedPassword) {
        this.$refs.confirmedPassword.focus();
        return;
      }

      this.setLoading(true);
      var result = await this.changePassword({
        username: this.account.username,
        password: SHA256(this.account.password),
        newPassword: SHA256(this.account.newPassword)
      });
      if (result == null) {
        this.$message.error(
          'Can not access to server. Please check your network!'
        );
      } else {
        if (result.success) {
          this.$message.success(result.message);
          this.$refs.form.reset();
          this.emitCloseState();
        } else {
          this.$message.error(result.message);
        }
      }

      this.setLoading(false);
    }
  }
};
</script>
