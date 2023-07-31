import { breakpointsTailwind, useBreakpoints } from '@vueuse/core';

const useLayout = () => {
  const breakpoints = useBreakpoints(breakpointsTailwind);
  const isMobile = breakpoints.smaller('sm');

  return { isMobile };
};
export default useLayout;
