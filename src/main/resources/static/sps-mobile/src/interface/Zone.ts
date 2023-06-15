export interface Zone {
	fee: number;
	id: number;
	title: string;
	tag: string;
	address: string;
	lng: number;
	lat: number;
	numberOfSpaces: number;
	availableSpaces: number;
	startsAt: string;
	endsAt: string;
	spaceList: Space[];
}

export interface Space {
	id: number;
	number: number;
	state: "AVAILABLE" | "TAKEN";
}

export interface OfficerSchedule {
	startsAt: string;
	endsAt: string;
	zones: Array<ScheduleZone>;
	daysOfWeek: Array<string>;
}
export interface ScheduleZone {
	id: number;
	tag: string;
	title: string;
	fee: number;
	location: { address: string; lng: number; lat: number };
	numberOfSpaces: number;
	startsAt: string;
	endsAt: string;
	spaces: Space[];
}
