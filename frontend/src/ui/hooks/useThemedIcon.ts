import { useCallback } from 'react';
import { Icons } from '../../assets/icons';
import { useTheme } from '../theme/useTheme';

export const useThemedIcon = () => {
   const { theme } = useTheme();

   const getIcon = useCallback((iconName: keyof typeof Icons.light) => {
      return theme === 'dark' ? Icons.light[iconName] : Icons.dark[iconName];
   }, [theme]);

   return { getIcon };
};