import { Context } from "https://deno.land/x/oak@v11.1.0/context.ts";
import { ObjectId } from "https://deno.land/x/web_bson@v0.2.5/mod.ts";
import checkDataRepository from "./check_data.repository.ts";

const getCheckData = async (
  context: Context<Record<string, any>, Record<string, any>>,
) => {
  try {
    const user = context.state.user as { userId: ObjectId };

    const checkDatas = await checkDataRepository.getCheckDatas(user.userId);

    context.response.body = {
      status: 200,
      message: "Successfully get data",
      payload: checkDatas,
    };
  } catch {
    context.response.body = {
      status: 400,
      message: "Failed to get data",
    };
  }
};

export default {
  getCheckData,
};
