import React from 'react';
import { FileQuestion } from 'lucide-react';

export default function EmptyState({ message = 'Awaiting Document Upload...' }) {
  return (
    <div className="flex flex-col items-center justify-center p-8 h-64 border-2 border-dashed border-slate-200 rounded-2xl bg-slate-50/50">
      <FileQuestion className="w-12 h-12 text-slate-300 mb-4" />
      <p className="text-slate-500 font-medium text-sm text-center">{message}</p>
    </div>
  );
}
