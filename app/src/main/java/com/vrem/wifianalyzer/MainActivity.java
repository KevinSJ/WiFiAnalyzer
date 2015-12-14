/*
 *    Copyright (C) 2010 - 2015 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.vrem.wifianalyzer;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.vrem.wifianalyzer.wifi.Details;
import com.vrem.wifianalyzer.wifi.Scanner;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private Scanner scanner;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        this.swipeRefreshLayout.setOnRefreshListener(new ListViewOnRefreshListener());

        this.listView = (ListView) findViewById(R.id.listView);
        this.listView.setAdapter(new ListViewAdapter(this));
        this.listView.setOnItemClickListener(new ListViewOnItemClickListener());

        scanner = Scanner.performPeriodicScans(this, (WifiManager) getSystemService(Context.WIFI_SERVICE), this.listView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return item.getItemId() == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    class ListViewOnItemClickListener implements  OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Details details = (Details) listView.getItemAtPosition(position);
            Toast.makeText(getApplicationContext(), details.toString(), Toast.LENGTH_LONG).show();
        }
    }

    class ListViewOnRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            swipeRefreshLayout.setRefreshing(true);
            scanner.update();
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}