export const toggled = state => state.drawerToggled;
export const isLoading = state => state.loading;
export const snackbarConfig = state => {
  return (
    state.snackbar.options || {
      color: 'green',
      timeout: 5000,
      'multi-line': true,
      bottom: false,
      left: false,
      right: true,
      top: true
    }
  );
};
