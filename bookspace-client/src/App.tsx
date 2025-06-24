import './App.css'
import {Route, Routes} from "react-router";
import BaseLayout from "@/layouts/BaseLayout.tsx";
import Home from "@/features/home/pages/Home.tsx";
import BookDetails from "@/features/books/pages/BookDetails.tsx";
import UserDetails from "@/features/users/pages/UserDetails";
import UserSettings from "@/features/users/pages/UserSettings";

function App() {

    return (
        <Routes>
            <Route element={<BaseLayout/>}>
                <Route index element={<Home />} />
                <Route path={"my-books"} element={<BookDetails/>}/>
                <Route path={"user/details"} element={<UserDetails/>}/>
                <Route path={"user/settings"} element={<UserSettings/>}/>
            </Route>
        </Routes>
    )
}

export default App
