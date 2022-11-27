import { Context } from "https://deno.land/x/oak@v11.1.0/context.ts";
import { ObjectId } from "https://deno.land/x/web_bson@v0.2.5/mod.ts";
import { CheckDataModel } from "./check_data.model.ts";
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

const createCheckData = async (
  context: Context<Record<string, any>, Record<string, any>>,
) => {
  try {
    const user = context.state.user as { userId: ObjectId };

    const body = context.request.body();

    const {
      oxygenSaturation,
      pulse,
      date,
    } = await body.value;

    const newCheckData: CheckDataModel = {
      userId: user.userId,
      oxygenSaturation,
      pulse,
      date,
    };

    await checkDataRepository.createCheckData(newCheckData);

    context.response.body = {
      status: 200,
      message: "Successfully to create data",
    };
  } catch {
    context.response.body = {
      status: 400,
      message: "Failed to create data",
    };
  }
};

export default {
  getCheckData,
  createCheckData,
};
