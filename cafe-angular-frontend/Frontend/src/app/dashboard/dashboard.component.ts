import { Component, AfterViewInit } from '@angular/core';
import { error } from 'console';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { DashboardService } from '../services/dashboard.service';
import { SnackbarService } from '../services/snackbar.service';
import { GlobalConstants } from '../shared/GlobalConstants';
@Component({
	selector: 'app-dashboard',
	templateUrl: './dashboard.component.html',
	styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements AfterViewInit {

	responseMessage:any;
	data:any;

	ngAfterViewInit() { }

	constructor(
		private dashboardSerivice:DashboardService,
		private ngxService:NgxUiLoaderService,
		private snackbarservice:SnackbarService
		) 
	{ this.ngxService.start();
	this.dashboardData();
	}

	dashboardData(){
		this.dashboardSerivice.getDetails().subscribe((response:any)=>{
			this.ngxService.stop();
			this.data = response;
		},(error:any)=>{
			this.ngxService.stop();
			console.log(error);
			if(error.error?.messgae){
                   this.responseMessage= error.error?.messgae;
			}else{
				this.responseMessage = GlobalConstants.genericError;
			}
		this.snackbarservice.openSnackBar(this.responseMessage, GlobalConstants.error);
		})
	}
}
