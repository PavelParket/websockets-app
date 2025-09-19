import type { RootState } from "../store/store"; // Adjust the path if your store file is elsewhere
import { useSelector } from "react-redux";
import { Navigate, Outlet, useLocation } from "react-router-dom";

interface ProtectedRouteProps {
   roles?: string[];
}

export function ProtectedRoute({ roles }: ProtectedRouteProps) {
   const { id, role } = useSelector((state: RootState) => state.auth);
   const location = useLocation();

   if (!id) {
      return <Navigate to="/login" state={{ from: location }} replace />;
   }

   if (roles && role && !roles.includes(role)) {
      return <Navigate to="/forbidden" replace />;
   }

   return <Outlet />;;
}
