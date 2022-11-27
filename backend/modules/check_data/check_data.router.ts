import { Router } from "https://deno.land/x/oak@v11.1.0/router.ts";
import authMiddleware from "../auth/auth.middleware.ts";
import checkDataController from "./check_data.controller.ts";

const router = new Router();

router.use(authMiddleware.authentication);
router.post("/get-check-data", checkDataController.getCheckData);
router.post("/create-check-data", checkDataController.createCheckData);

export default router.routes();
