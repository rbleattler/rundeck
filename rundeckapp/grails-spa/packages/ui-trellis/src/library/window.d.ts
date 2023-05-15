import {RundeckContext} from '@/library'
import {AppLinks} from "./interfaces/AppLinks";

declare global {
    interface Window {
        _rundeck: RundeckContext
        appLinks: AppLinks
    }
}