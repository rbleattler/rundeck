import moment, {MomentInput} from "moment";

export function fmtDate_full(val: MomentInput) {
    return moment(val).format("MM/DD/YYYY hh:mm a")
}

export function formatFromNow(val:MomentInput) {
    return moment(val).fromNow()
}