import './App.css'
import {Route, Routes} from "react-router";
import BaseLayout from "@/layouts/BaseLayout.tsx";
import Home from "@/features/home/pages/Home.tsx";
import BookDetails from "@/features/books/pages/BookDetails.tsx";
import UserDetails from "@/features/users/pages/UserDetails";
import UserSettings from "@/features/users/pages/UserSettings";
import AdminLayout from "@/layouts/AdminLayout.tsx";
import {AuthProvider} from "@/features/auth/AuthContext";
import ProtectedRoute from "@/features/auth/ProtectedRoute";
import Login from "@/features/auth/pages/Login.tsx";
import SignUp from "@/features/auth/pages/sign-up.tsx";
import BookTable from "@/features/admin/pages/books/book-table.tsx";

function App() {
    return (
        <AuthProvider>
            <Routes>
                <Route element={<BaseLayout/>}>
                    <Route index element={<Home/>}/>
                    <Route path={"book/:id"} element={<BookDetails/>}/>
                    <Route path={"user"}>
                        <Route path={":name"} element={<UserDetails/>}/>
                        <Route path={":name/settings"} element={<ProtectedRoute>
                            <UserSettings/>
                        </ProtectedRoute>}>

                        </Route>
                    </Route>
                </Route>

                <Route path={"admin"} element={
                    <ProtectedRoute requiredRole={"ADMIN"}>
                        <AdminLayout/>
                    </ProtectedRoute>
                }>
                    <Route index element={<BookTable/>}/>
                </Route>
                <Route path={"/login"} element={<Login/>}/>
                <Route path={"/sign-up"} element={<SignUp/>}/>
            </Routes>
        </AuthProvider>
    )
}

export default App
