import { RootState } from "../store";
import { Room } from "@/types/room";
import { ChatMessage } from "@/types/chat_message";
import { createSlice, PayloadAction } from "@reduxjs/toolkit";

const initialState: { room: Room } = {
  room: {
    messages: [],
    userCount: 0,  // Added userCount to the room object
  },
};

const roomSlice = createSlice({
  name: "room",
  initialState,
  reducers: {
    setRoom(state, action: PayloadAction<Room>) {
      state.room = action.payload;
    },
    addMessageToRoom(state, action: PayloadAction<ChatMessage>) {
      state.room.messages.push(action.payload);
    },
    setUserCount(state, action: PayloadAction<number>) {
      state.room.userCount = action.payload;  // Set the user count
    },
  },
});

export const { setRoom, addMessageToRoom, setUserCount } = roomSlice.actions;
export const selectRoom = (state: RootState) => state.room.room;
export default roomSlice.reducer;
