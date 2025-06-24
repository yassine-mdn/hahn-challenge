import './App.css'
import {Route, Routes} from "react-router";
import BaseLayout from "@/layouts/BaseLayout.tsx";
import Home from "@/routes/Home.tsx";
import BookDetails from "@/routes/BookDetails.tsx";

function App() {

    return (
        <Routes>
            <Route element={<BaseLayout/>}>
                <Route index element={<Home />} />
                <Route path={"my-books"} element={<BookDetails/>}/>
            </Route>
        </Routes>
    )
}

export default App
