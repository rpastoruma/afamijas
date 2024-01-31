import { Component, Input, OnInit } from '@angular/core';
import { NbDialogRef } from '@nebular/theme';

@Component({
  selector: 'app-delete-confirm',
  templateUrl: './delete-confirm.component.html',
  styleUrls: ['./delete-confirm.component.scss']
})
export class DeleteConfirmComponent implements OnInit {

  @Input() text;
  @Input() value;

  constructor(protected ref: NbDialogRef<DeleteConfirmComponent>) { }

  ngOnInit(): void {
  }

  confirm() {
    this.ref.close('confirm');
  }

  dismiss() {
    this.ref.close('close');
  }
}
