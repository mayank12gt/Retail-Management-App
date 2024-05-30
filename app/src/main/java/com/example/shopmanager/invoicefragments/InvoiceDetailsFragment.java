package com.example.shopmanager.invoicefragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopmanager.R;
import com.example.shopmanager.adapters.InvoiceItemsAdapter;
import com.example.shopmanager.models.Customer;
import com.example.shopmanager.models.InventoryItem;
import com.example.shopmanager.models.PaidStatus;
import com.example.shopmanager.models.invoice.Invoice;
import com.example.shopmanager.models.invoice.InvoiceItem;
import com.example.shopmanager.utils.Utils;
import com.example.shopmanager.viewmodels.InvoicesViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.glxn.qrgen.android.QRCode;
import net.glxn.qrgen.core.image.ImageType;

import java.io.File;
import java.util.List;


public class InvoiceDetailsFragment extends Fragment implements SetupInvoiceItemListItemLayout {



    private static final String ARG_INVOICE = "invoice";
    private static final String ARG_INVOICE_DETAILS_FRAG = "invoice_details_frag";


  Invoice invoice;
    TextView customerName;
    TextView grandTotal;
    TextView numItems;
    TextView customerContactNum;
    TextView date;
    ImageView upiQR;
    RecyclerView invoiceItemsRv;
    InvoicesViewModel viewModel;
    CardView pdf_card;
   MaterialButton changePaidStatusBtn;
    Chip paidStatusChip;


    public InvoiceDetailsFragment() {

    }


    public static InvoiceDetailsFragment newInstance(Invoice invoice) {
        InvoiceDetailsFragment fragment = new InvoiceDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_INVOICE, invoice);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            invoice = (Invoice) getArguments().getSerializable(ARG_INVOICE);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        viewModel = new ViewModelProvider(this).get(InvoicesViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      View v= inflater.inflate(R.layout.fragment_invoice_details, container, false);

        customerName = v.findViewById(R.id.customer_name);
        customerContactNum = v.findViewById(R.id.customer_contact_num);
        grandTotal = v.findViewById(R.id.invoice_grand_total);
        numItems = v.findViewById(R.id.invoice_num_items);
        date = v.findViewById(R.id.invoice_date);
        invoiceItemsRv = v.findViewById(R.id.invoice_items_rv);
        pdf_card = v.findViewById(R.id.pdf_card_view);
        upiQR = v.findViewById(R.id.upi_qr);
        paidStatusChip = v.findViewById(R.id.paid_status_chip);
        changePaidStatusBtn = v.findViewById(R.id.paid_status_btn);



        Customer customer = viewModel.getCustomerfromId(invoice.getCustomer_id());
        List<InvoiceItem> items = viewModel.getInvoiceItemsfromId(invoice.getId());
        int num = viewModel.getTotalNumofItemsforInvoice(invoice.getId(),items);


        if(invoice.getStatus()== PaidStatus.PAID){
            paidStatusChip.setText("Paid");
            changePaidStatusBtn.setText("Mark as Unpaid");
            paidStatusChip.setBackgroundColor(getResources().getColor(R.color.green_1,getActivity().getTheme()));

        } else if (invoice.getStatus()== PaidStatus.UNPAID) {
            paidStatusChip.setText("Unpaid");
            changePaidStatusBtn.setText("Mark as Paid");
            paidStatusChip.setBackgroundColor(getResources().getColor(R.color.red_1,getActivity().getTheme()));
        }

        changePaidStatusBtn.setOnClickListener(view -> {
            if(invoice.getStatus()== PaidStatus.PAID){
                invoice.setStatus(PaidStatus.UNPAID);
                changePaidStatusBtn.setText("Mark as Paid");
                changePaidStatusBtn.setTextColor(getResources().getColor(R.color.red_1, getActivity().getTheme()));
                paidStatusChip.setChipBackgroundColorResource(R.color.red_1);
            paidStatusChip.setText("Unpaid");
            } else if (invoice.getStatus()== PaidStatus.UNPAID) {
                invoice.setStatus(PaidStatus.PAID);
                changePaidStatusBtn.setText("Mark as Unpaid");
                changePaidStatusBtn.setTextColor(getResources().getColor(R.color.green_1,getActivity().getTheme()));
                paidStatusChip.setChipBackgroundColorResource(R.color.green_1);
                paidStatusChip.setText("Paid");
            }
            viewModel.updateInvoice(invoice);
        });

        customerName.setText(customer.getName());
        customerContactNum.setText(String.valueOf(customer.getContactNum()));
        grandTotal.setText("₹ "+String.valueOf(invoice.getTotal()));
        date.setText(Utils.formatDate(invoice.getTimeStamp()));
        numItems.setText(String.valueOf(num)+" items");
        setQR();

        items.forEach(item -> {
            Log.d("invoice items",String.valueOf(item.getInvoice_id()));
        });

        InvoiceItemsAdapter itemsAdapter = new InvoiceItemsAdapter(items,this,ARG_INVOICE_DETAILS_FRAG);
        invoiceItemsRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        invoiceItemsRv.setAdapter(itemsAdapter);

        pdf_card.setOnClickListener(view -> {

            String pdfFilePath = viewModel.getPdfPathfromId(invoice.getId());
            String authority = "com.example.shopmanager.provider";

            Uri fileUri = FileProvider.getUriForFile(getActivity(), authority, new File(pdfFilePath));

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(fileUri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {

                Toast.makeText(getActivity(), "No application available to open PDF", Toast.LENGTH_SHORT).show();
            }


        });

    return v;
    }

    private void setQR() {
        Bitmap qrCode = QRCode.from(invoice.getUpiUri()).to(ImageType.PNG).withSize(250, 250).bitmap();
        upiQR.setImageBitmap(qrCode);
    }

    @Override
    public void SetupInvoiceItemListItemLayout(List<InvoiceItem> items, int position, TextView itemNo, TextView itemName, TextView itemQty, TextView itemTotal) {
        InvoiceItem item = items.get(position);

        InventoryItem inventoryItem = viewModel.getItemfromId(item.getInvoice_item_id());
        itemNo.setText(String.valueOf(position+1));
        itemName.setText(inventoryItem.getName());
        itemQty.setText(String.valueOf("x"+item.getItemQty()));
        itemTotal.setText(String.valueOf(inventoryItem.getSellingPrice()*item.getItemQty()));
    }
}