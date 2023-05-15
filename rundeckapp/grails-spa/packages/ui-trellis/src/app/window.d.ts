import {RundeckContext} from '@/library'
import {AppLinks} from "../library";

declare global {
    interface Window {
        _rundeck: RundeckContext
        appLinks: AppLinks
    }
}