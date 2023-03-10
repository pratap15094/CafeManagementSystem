import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { SnackbarService } from 'src/app/services/snackbar.service';
import { UserService } from 'src/app/services/user.service';
import { GlobalConstants } from 'src/app/shared/GlobalConstants';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.scss']
})
export class ChangePasswordComponent implements OnInit {

  oldPassword = true;
  newPassword = true;
  confirmPassword = true;
  changePassword: any = FormGroup;
  responseMessage: any;

  constructor(
    private formBuilder: FormBuilder,
    private userService: UserService,
    public dialogRef: MatDialogRef<ChangePasswordComponent>,
    private ngxService: NgxUiLoaderService,
    private snackbarService: SnackbarService
  ) { }

  ngOnInit(): void {
    this.changePassword = this.formBuilder.group({
      oldPassword: [null, Validators.required],
      newPassword: [null, Validators.required],
      confirmPassword: [null, Validators.required]
    })
  }

  validateSubmit() {
    if (this.changePassword.controls['newPassword'].value != this.changePassword.controls['confirmPassword'].value) {
      return true;
    } else {
      return false;
    }
  }

  handlepasswordChangeSubmit() {
    this.ngxService.start();
    var formData = this.changePassword.value;
    var data = {
      oldPassword: formData.oldPassword,
      newPassword: formData.newPassword,
      confirmPassword: formData.confirmPasswor
    }

    this.userService.changePassword(data).subscribe((response: any) => {
      this.ngxService.stop();
      this.responseMessage = response?.message;
      this.dialogRef.close();
      this.snackbarService.openSnackBar(this.responseMessage, "success");
    }, (error) => {
      console.log(Error);
      this.ngxService.stop();
      if (error.error?.message) {
        this.responseMessage = error.error?.message;
      } else {
        this.responseMessage = GlobalConstants.genericError;
      }
      this.snackbarService.openSnackBar(this.responseMessage, GlobalConstants.error);
    })

  }
}

