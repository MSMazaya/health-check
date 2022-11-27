import { ObjectId } from "https://deno.land/x/web_bson@v0.2.5/mod.ts";

export interface CheckDataModel {
  userId: ObjectId;
  oxygenSaturation: number;
  pulse: number;
  // Should be iso date string, too lazy to work with time based on mongo
  date: string;
}

export type CheckDataSchema = CheckDataModel & {
  _id: ObjectId;
};
